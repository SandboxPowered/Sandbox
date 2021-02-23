package org.sandboxpowered.loader.loading;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import com.github.zafarkhaja.semver.Parser;
import com.github.zafarkhaja.semver.expr.Expression;
import com.github.zafarkhaja.semver.expr.ExpressionParser;
import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sandboxpowered.api.addon.Addon;
import org.sandboxpowered.api.block.BaseBlock;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.item.BaseBlockItem;
import org.sandboxpowered.api.item.BlockItem;
import org.sandboxpowered.api.registry.Registry;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.util.Side;
import org.sandboxpowered.internal.AddonSpec;
import org.sandboxpowered.loader.loading.resource_service.GlobalResourceService;
import org.sandboxpowered.loader.packs.AddonPackRespositorySource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static org.sandboxpowered.loader.loading.AddonFinder.SANDBOX_TOML;

public class SandboxLoader {
    private static final Parser<Expression> PARSER = ExpressionParser.newInstance();
    private final Map<String, AddonSpec> loadedAddons = new HashMap<>();
    private final Map<AddonSpec, Addon> addonMap = new HashMap<>();
    private final Map<AddonSpec, AddonSpecificAPIReference> addonAPIs = new HashMap<>();
    private final Map<AddonSpec, AddonSpecificRegistrarReference> addonRegistrars = new HashMap<>();
    private final Map<String, AddonClassLoader> addonToClassLoader = new LinkedHashMap<>();
    public Logger log = LogManager.getLogger(SandboxLoader.class);
    public AddonPackRespositorySource addonPackRespositorySource = new AddonPackRespositorySource();
    private boolean loaded;
    private final GlobalResourceService resourceService = new GlobalResourceService();

    private AddonSpecificRegistrarReference getRegistrarForAddon(AddonSpec spec) {
        return addonRegistrars.computeIfAbsent(spec, s -> new AddonSpecificRegistrarReference(s, this));
    }

    public GlobalResourceService getResourceService() {
        return resourceService;
    }

    private AddonSpecificAPIReference getAPIForAddon(AddonSpec spec) {
        return addonAPIs.computeIfAbsent(spec, s -> new AddonSpecificAPIReference(s, this));
    }

    public Optional<AddonSpec> getAddon(String addonId) {
        return Optional.ofNullable(loadedAddons.get(addonId));
    }

    public void loadAddon(AddonSpec info, Addon addon) {
        loadedAddons.put(info.getId(), info);
        addonMap.put(info, addon);
    }

    private List<AddonSpec> getLoadOrder() {
        Set<AddonSpec> visited = new HashSet<>();
        List<AddonSpec> loadOrder = new ArrayList<>();
        for (AddonSpec info : getAllAddons().keySet()) {
            handleDependencies(visited, loadOrder, info);
        }
        return loadOrder;
    }

    private void loopInOrder(Consumer<AddonSpec> consumer) {
        getLoadOrder().forEach(spec -> {
            if (!spec.getPlatformSupport(getPlatform()).canRun()) {
                throw new IllegalStateException(String.format("Addon %s cannot run on platform %s!", spec.getId(), getPlatform().toString()));
            }
            try {
                consumer.accept(spec);
            } catch (Exception e) {
                throw new RuntimeException(String.format("Loading for addon %s failed: %s", spec.getId(), e.getMessage()), e);
            }
        });
    }

    public Identity getPlatform() {
        return Identity.of("sandbox", "test");
    }

    private void handleDependencies(Set<AddonSpec> visited, List<AddonSpec> order, AddonSpec spec) {
        if (visited.contains(spec)) return;
        visited.add(spec);
        Map<String, String> dependencies = spec.getDependencies();
        for (String dep : dependencies.keySet()) {
            Optional<AddonSpec> optionalDep = getAddon(dep);
            if (!optionalDep.isPresent())
                throw new IllegalStateException(String.format("Addon %s depends on other addon %s that isn't loaded!", spec.getId(), dep));
            AddonSpec dependency = optionalDep.get();
            String versionString = dependencies.get(dep);
            Expression version = PARSER.parse(versionString);
            if (!version.interpret(dependency.getVersion()))
                throw new IllegalStateException(String.format("Addon %s depends on %s version %s but found version %s instead!", spec.getId(), dep, versionString, dependency.getVersion().toString()));
            handleDependencies(visited, order, dependency);
        }
        order.add(spec);
    }

    public Map<AddonSpec, Addon> getAllAddons() {
        return ImmutableMap.copyOf(addonMap);
    }

    public void unload() {
        log.info("Unloading Sandbox");
        resourceService.clear();
        loadedAddons.clear();
        addonMap.clear();
        addonAPIs.clear();
        addonRegistrars.clear();
        addonToClassLoader.clear();
        loaded = false;
    }

    public AddonClassLoader getClassLoader(AddonSpec spec, URL url) {
        return addonToClassLoader.computeIfAbsent(spec.getId(), addonId -> new AddonClassLoader(this, Addon.class.getClassLoader(), url, spec));
    }

    private void loadFromURLs(Collection<URL> urls) {
        if (urls.isEmpty()) {
            log.info("Loaded 0 addons");
        } else {
            log.info("Loading {} addons", urls.size());
            TomlParser parser = new TomlParser();
            for (URL url : urls) {
                InputStream configStream = null;
                JarFile jarFile = null;
                try {
                    if (url.toString().endsWith(".jar")) {
                        jarFile = new JarFile(new File(url.toURI()));
                        ZipEntry ze = jarFile.getEntry(SANDBOX_TOML);
                        if (ze != null)
                            configStream = jarFile.getInputStream(ze);
                    } else {
                        configStream = url.toURI().resolve(SANDBOX_TOML).toURL().openStream();
                    }
                    if (configStream == null)
                        continue;
                    Config config = parser.parse(configStream);
                    AddonSpec spec = AddonSpec.from(config, url);
                    AddonClassLoader loader = getClassLoader(spec, url);
                    Class<?> mainClass = loader.loadClass(spec.getMainClass());
                    Object obj = mainClass.getConstructor().newInstance();
                    if (obj instanceof Addon) {
                        loadAddon(spec, (Addon) obj);
                    } else {
                        log.error("Unable to load addon '{}', main class not instance of Addon", spec.getId());
                    }
                } catch (IOException | NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | URISyntaxException e) {
                    log.error("Unknown Error", e);
                    //TODO: Split these up to provide unique messages per error.
                } finally {
                    IOUtils.closeQuietly(configStream);
                    IOUtils.closeQuietly(jarFile);
                }
            }
        }
    }

    public void load(MinecraftServer server, LevelStorageSource.LevelStorageAccess storageSource) {
        Path path = storageSource.getLevelPath(LevelResource.ROOT);
        Path addonsDir = path.resolve("addons");
        AddonFinder finder = new AddonFinder.MergedScanner(
                new AddonFinder.FolderScanner(addonsDir),
                new AddonFinder.ClasspathScanner()
        );
        try {
            loadFromURLs(finder.findAddons());
        } catch (IOException e) {
            log.error("Failed to load classpath addons", e);
        }

        resourceService.initVanillaContent();

        loopInOrder(spec -> addonMap.get(spec).init(getAPIForAddon(spec)));
        loopInOrder(spec -> addonMap.get(spec).register(getAPIForAddon(spec), getRegistrarForAddon(spec)));
        loopInOrder(spec -> addonMap.get(spec).finishLoad(getAPIForAddon(spec)));
        loaded = true;

        resourceService.getResourceMap().forEach((material, subMap) -> subMap.forEach((type, resource) -> {
            Content o = resource.get();
            Set set = resource.getVariants();
            if (o != null) {
                if (o.getIdentity().getNamespace().equals("minecraft")) {
                    if (set.size() > 1) {
                        log.info(String.format("Ignoring %d variants for '%s:%s' as '%s' already exists.", set.size() - 1, material, type, o.getIdentity()));
                    }
                } else {
                    register(o);
                }
            }
            }));

        if (server instanceof IntegratedServer) {
            reloadClientResources();
        }
        server.getPackRepository().reload();
        server.reloadResources(server.getPackRepository().getSelectedIds());
    }

    @CanIgnoreReturnValue
    public  <C extends Content<C>> Registry.Entry<C> register(C content) {
        Registry.Entry<C> entry = Registry.getRegistryFromType(content.getContentType()).register(content);
        if (content instanceof BaseBlock) {
            BlockItem item = ((BaseBlock) content).createBlockItem();
            if (item instanceof BaseBlockItem) {
                Registry.getRegistryFromType(item.getContentType()).register(item.setIdentity(content.getIdentity()));
            }
        }
        return entry;
    }

    private void reloadClientResources() {
        Minecraft.getInstance().reloadResourcePacks();
    }

    public boolean isAddonLoaded(String addonId) {
        return getAddon(addonId).isPresent();
    }

    public Side getSide() {
        return Side.SERVER;
    }

    public boolean isExternalModLoaded(String loader, String modId) {
        return false;
    }

    public boolean isLoaded() {
        return loaded;
    }
}