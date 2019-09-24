package org.sandboxpowered.sandbox.fabric.loader;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import org.sandboxpowered.sandbox.api.SandboxAPI;
import org.sandboxpowered.sandbox.api.addon.Addon;
import org.sandboxpowered.sandbox.api.addon.AddonSpec;
import org.sandboxpowered.sandbox.fabric.security.AddonClassLoader;
import net.fabricmc.loader.util.UrlUtil;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class SandboxLoader {

    private final SandboxAPI api;
    private final List<Path> fileAddonsToLoad;
    private final Executor executor = Executors.newCachedThreadPool();
    private final Map<String, AddonClassLoader> modidToLoader = new LinkedHashMap<>();
    private final List<AddonSpec> addons = new LinkedList<>();

    public SandboxLoader(SandboxAPI api, List<Path> fileAddonsToLoad) {
        this.api = api;
        this.fileAddonsToLoad = fileAddonsToLoad;
    }

    public List<AddonSpec> getAddons() {
        return addons;
    }

    public void load() throws IOException {
        load(true);
    }

    public void load(boolean b) throws IOException {
        addons.clear();
        modidToLoader.clear();

        Set<URL> urls = new HashSet<>();
        fileAddonsToLoad.forEach(path -> {
            try {
                urls.add(path.toUri().toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });

        TomlParser parser = new TomlParser();
        Enumeration<URL> enumeration = getClass().getClassLoader().getResources("sandbox.toml");
        while (enumeration.hasMoreElements()) { // Add it all to a set to temporarily remove duplicates
            try {
                urls.add(UrlUtil.getSource("sandbox.toml", enumeration.nextElement()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        urls.forEach(cURL -> {
            executor.execute(() -> {
                InputStream configStream = null;
                try {
                    if (cURL.toString().endsWith(".sbx")) {
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
                    Class mainClass = getClassLoader(spec).loadClass(spec.getMainClass());
                    if (!Addon.class.isAssignableFrom(mainClass)) {
                        return;
                    }
                    addons.add(spec);
                    Addon addon = (Addon) mainClass.getConstructor().newInstance();
                    addon.init(api);
                    if (b)
                        addon.register();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(configStream);
                }
            });
        });
    }

    public AddonClassLoader getClassLoader(AddonSpec spec) {
        return modidToLoader.computeIfAbsent(spec.getModid(), modid -> new AddonClassLoader(getClass().getClassLoader(), spec));
    }
}