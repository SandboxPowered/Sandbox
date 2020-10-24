package org.sandboxpowered.sandbox.fabric.loader;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import net.fabricmc.loader.util.UrlConversionException;
import net.fabricmc.loader.util.UrlUtil;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.IOUtils;
import org.sandboxpowered.api.addon.Addon;
import org.sandboxpowered.internal.AddonSpec;
import org.sandboxpowered.sandbox.fabric.SandboxFabric;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.security.AddonClassLoader;
import org.sandboxpowered.sandbox.fabric.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

public class SandboxLoader {
    private static final String SANDBOX_TOML = "sandbox.toml";
    public static SandboxLoader loader;
    private final Map<String, AddonClassLoader> modidToLoader = new LinkedHashMap<>();
    private SandboxFabric fabric;

    public SandboxLoader() {
        loader = this;
    }

    public SandboxFabric getFabric() {
        return fabric;
    }

    public void load() throws IOException {
        if (fabric != null)
            fabric.destroy();
        fabric = new SandboxFabric();
        modidToLoader.clear();

        Registry.REGISTRIES.stream().map(registry -> (SandboxInternal.Registry) registry).forEach(SandboxInternal.Registry::sandboxStore);

        Path addonPath = Paths.get("addons");
        if (Files.notExists(addonPath)) Files.createDirectories(addonPath);
        Set<URL> addonUrls;
        try (Stream<Path> stream = Files.walk(addonPath, 1)) {
            addonUrls = stream.filter(path -> path.toString().endsWith(".jar"))
                    .map(path -> {
                        try {
                            return path.toUri().toURL();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toSet());
        }

        Enumeration<URL> enumeration = getClass().getClassLoader().getResources(SANDBOX_TOML);
        while (enumeration.hasMoreElements()) { // Add it all to a set to temporarily remove duplicates
            try {
                addonUrls.add(UrlUtil.getSource(SANDBOX_TOML, enumeration.nextElement()));
            } catch (UrlConversionException e) {
                Log.error("Error converting URL", e);
            }
        }

        if (addonUrls.isEmpty()) {
            Log.info("Loaded 0 addons");
        } else {
            Log.info("Loading %d addons", addonUrls.size());
            TomlParser parser = new TomlParser();
            for (URL cURL : addonUrls) {
                InputStream configStream = null;
                try {
                    if (cURL.toString().endsWith(".jar")) {
                        try (JarFile jarFile = new JarFile(new File(cURL.toURI()))) {
                            ZipEntry ze = jarFile.getEntry(SANDBOX_TOML);
                            if (ze != null)
                                configStream = jarFile.getInputStream(ze);
                        }
                    } else {
                        configStream = cURL.toURI().resolve(SANDBOX_TOML).toURL().openStream();
                    }
                    if (configStream == null)
                        continue;
                    Config config = parser.parse(configStream);
                    AddonSpec spec = AddonSpec.from(config, cURL);
                    getClassLoader(spec).addURL(cURL);
                    Class<?> mainClass = getClassLoader(spec).loadClass(spec.getMainClass());
                    if (Addon.class.isAssignableFrom(mainClass)) {
                        Addon addon = (Addon) mainClass.getConstructor().newInstance();
                        fabric.loadAddon(spec, addon);
                    }
                } catch (Exception e) {
                    Log.error("Unknown Error", e);
                } finally {
                    IOUtils.closeQuietly(configStream);
                }
            }

            fabric.initAll();
            fabric.registerAll();
            fabric.reloadResources();
        }
    }

    public AddonClassLoader getClassLoader(AddonSpec spec) {
        return modidToLoader.computeIfAbsent(spec.getId(), addonId -> new AddonClassLoader(getClass().getClassLoader(), spec));
    }
}