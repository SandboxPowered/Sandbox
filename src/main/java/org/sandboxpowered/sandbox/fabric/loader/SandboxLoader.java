package org.sandboxpowered.sandbox.fabric.loader;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import net.fabricmc.loader.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.sandboxpowered.api.addon.Addon;
import org.sandboxpowered.internal.AddonSpec;
import org.sandboxpowered.sandbox.fabric.SandboxFabric;
import org.sandboxpowered.sandbox.fabric.security.AddonClassLoader;
import org.sandboxpowered.sandbox.fabric.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class SandboxLoader {
    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    private final Map<String, AddonClassLoader> modidToLoader = new LinkedHashMap<>();
    private SandboxFabric fabric;

    public void load() throws IOException {
        if (fabric != null)
            fabric.destroy();
        fabric = new SandboxFabric();
        modidToLoader.clear();

        Set<URL> addonUrls = new HashSet<>();
        Enumeration<URL> enumeration = getClass().getClassLoader().getResources("sandbox.toml");
        while (enumeration.hasMoreElements()) { // Add it all to a set to temporarily remove duplicates
            try {
                addonUrls.add(UrlUtil.getSource("sandbox.toml", enumeration.nextElement()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!addonUrls.isEmpty()) {
            Log.info("Loading %d addons", addonUrls.size());
            TomlParser parser = new TomlParser();
            addonUrls.forEach(cURL -> {
                InputStream configStream = null;
                try {
                    if (cURL.toString().endsWith(".jar")) {
                        JarFile jarFile = new JarFile(new File(cURL.toURI()));
                        ZipEntry ze = jarFile.getEntry("sandbox.toml");
                        if (ze != null)
                            configStream = jarFile.getInputStream(ze);
                    } else {
                        configStream = cURL.toURI().resolve("sandbox.toml").toURL().openStream();
                    }
                    if (configStream == null)
                        return;
                    Config config = parser.parse(configStream);
                    AddonSpec spec = AddonSpec.from(config, cURL);
                    getClassLoader(spec).addURL(cURL);
                    Class<?> mainClass = getClassLoader(spec).loadClass(spec.getMainClass());
                    if (!Addon.class.isAssignableFrom(mainClass)) {
                        return;
                    }
                    Addon addon = (Addon) mainClass.getConstructor().newInstance();
                    fabric.loadAddon(spec, addon);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(configStream);
                }
            });

            fabric.initAll();
            fabric.registerAll();
        }
    }

    public AddonClassLoader getClassLoader(AddonSpec spec) {
        return modidToLoader.computeIfAbsent(spec.getId(), addonId -> new AddonClassLoader(getClass().getClassLoader(), spec));
    }
}