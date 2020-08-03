package org.sandboxpowered.sandbox.fabric;

import com.github.zafarkhaja.semver.Parser;
import com.github.zafarkhaja.semver.expr.Expression;
import com.github.zafarkhaja.semver.expr.ExpressionParser;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.sandboxpowered.api.SandboxAPI;
import org.sandboxpowered.api.addon.Addon;
import org.sandboxpowered.api.addon.AddonInfo;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.registry.Registrar;
import org.sandboxpowered.api.registry.Registry;
import org.sandboxpowered.api.resources.ResourceManager;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.util.Log;
import org.sandboxpowered.api.util.Side;
import org.sandboxpowered.internal.Sandbox;
import org.sandboxpowered.sandbox.fabric.util.AddonLog;

import java.util.*;

public class SandboxFabric implements Sandbox {
    private static final Parser<Expression> PARSER = ExpressionParser.newInstance();
    private static final Identity PLATFORM = Identity.of("sandboxpowered", "fabric");

    private final Map<String, AddonInfo> loadedAddons = new LinkedHashMap<>();
    private final Map<AddonInfo, Addon> addonMap = new LinkedHashMap<>();
    private final Map<AddonInfo, SandboxAPI> addonAPIs = new LinkedHashMap<>();
    private final Map<AddonInfo, Registrar> addonRegistrars = new LinkedHashMap<>();

    public void loadAddon(AddonInfo info, Addon addon) {
        loadedAddons.put(info.getId(), info);
        addonMap.put(info, addon);
    }

    private List<AddonInfo> getLoadOrder() {
        Set<AddonInfo> visited = new HashSet<>();
        List<AddonInfo> loadOrder = new ArrayList<>();
        for (AddonInfo info : getAllAddons().keySet()) {
            handleDependencies(visited, loadOrder, info);
        }
        return loadOrder;
    }

    public void initAll() {
        getLoadOrder().forEach(info -> {
            if (!info.getPlatformSupport(getPlatform()).canRun()) {
                throw new IllegalStateException(String.format("Addon %s cannot run on platform %s!", info.getId(), getPlatform().toString()));
            }
            Addon addon = getAllAddons().get(info);
            SandboxAPI api = getAPIFor(info);
            try {
                addon.init(api);
            } catch (Exception e) {
                throw new RuntimeException(String.format("Initialization for addon %s failed: %s", info.getId(), e.getMessage()), e);
            }
        });
    }

    public void registerAll() {
        getLoadOrder().forEach(info -> {
            if (!info.getPlatformSupport(getPlatform()).canRun()) {
                throw new IllegalStateException(String.format("Addon %s cannot run on platform %s!", info.getId(), getPlatform().toString()));
            }
            Addon addon = getAllAddons().get(info);
            Registrar registrar = getRegistrarFor(info);
            try {
                addon.register(registrar);
            } catch (Exception e) {
                throw new RuntimeException(String.format("Registration for addon %s failed: %s", info.getId(), e.getMessage()), e);
            }
        });
        ResourceManager.register();
    }

    //TODO: does this properly prevent circular dependencies while satisfying everything?
    private void handleDependencies(Set<AddonInfo> visited, List<AddonInfo> order, AddonInfo info) {
        if (visited.contains(info)) return;
        visited.add(info);
        Map<String, String> dependencies = info.getDependencies();
        for (String dep : dependencies.keySet()) {
            Optional<AddonInfo> optionalDep = getAddon(dep);
            if (!optionalDep.isPresent())
                throw new IllegalStateException(String.format("Addon %s depends on other addon %s that isn't loaded!", info.getId(), dep));
            AddonInfo dependency = optionalDep.get();
            String versionString = dependencies.get(dep);
            Expression version = PARSER.parse(versionString);
            if (!version.interpret(dependency.getVersion()))
                throw new IllegalStateException(String.format("Addon %s depends on %s version %s but found version %s instead!", info.getId(), dep, versionString, dependency.getVersion().toString()));
            handleDependencies(visited, order, info);
        }
        order.add(info);
    }

    @Override
    public Identity getPlatform() {
        return PLATFORM;
    }

    @Override
    public Optional<AddonInfo> getAddon(String addonId) {
        return Optional.ofNullable(loadedAddons.get(addonId));
    }

    @Override
    public Map<AddonInfo, Addon> getAllAddons() {
        return ImmutableMap.copyOf(addonMap);
    }

    @Override
    public SandboxAPI getAPIFor(AddonInfo info) {
        return addonAPIs.computeIfAbsent(info, AddonSpecificAPI::new);
    }

    @Override
    public Registrar getRegistrarFor(AddonInfo info) {
        return addonRegistrars.computeIfAbsent(info, AddonSpecificRegistrar::new);
    }

    public void destroy() {
        loadedAddons.clear();
        addonMap.clear();
        addonAPIs.clear();
        addonRegistrars.clear();
    }

    public void reloadResources() {
        org.sandboxpowered.sandbox.fabric.Sandbox.SANDBOX.reload();
    }

    public static class AddonSpecificRegistrar implements Registrar {
        private final AddonInfo info;

        public AddonSpecificRegistrar(AddonInfo info) {
            this.info = info;
        }

        @Override
        public AddonInfo getSourceAddon() {
            return info;
        }

        @Override
        public <T extends Content<T>> Registry.Entry<T> getEntry(Identity identity, Class<T> tClass) {
            return getEntry(identity, Registry.getRegistryFromType(tClass));
        }

        @Override
        public <T extends Content<T>> Registry.Entry<T> getEntry(Identity identity, Registry<T> registry) {
            return registry.get(identity);
        }

        @Override
        public <T extends Content<T>> Registry.Entry<T> register(Identity identity, T content) {
            return Registry.getRegistryFromType(content.getContentType()).register(identity, content);
        }

        @Override
        public <T extends Service> Optional<T> getRegistrarService(Class<T> tClass) {
            return Optional.empty(); //TODO: Implement registrar services
        }
    }

    public class AddonSpecificAPI implements SandboxAPI {
        private final AddonInfo info;
        private final Log log;

        public AddonSpecificAPI(AddonInfo info) {
            this.info = info;
            this.log = new AddonLog(info);
        }

        @Override
        public boolean isAddonLoaded(String addonId) {
            return getAddon(addonId).isPresent();
        }

        @Override
        public boolean isExternalModLoaded(String loader, String modId) {
            if (!"fabric".equals(loader))
                return false;
            if (modId == null || modId.isEmpty())
                return true;
            return FabricLoader.getInstance().isModLoaded(modId);
        }

        @Override
        public AddonInfo getSourceAddon() {
            return info;
        }

        @Override
        public Side getSide() {
            return org.sandboxpowered.sandbox.fabric.Sandbox.SANDBOX.getSide();
        }

        @Override
        public Log getLog() {
            return log;
        }
    }
}