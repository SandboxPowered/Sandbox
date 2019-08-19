package com.hrznstudio.sandbox.loader;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import com.hrznstudio.sandbox.api.SandboxAPI;
import com.hrznstudio.sandbox.api.addon.Addon;
import com.hrznstudio.sandbox.security.AddonClassLoader;
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
    private AddonClassLoader loader;
    private Executor executor = Executors.newCachedThreadPool();
    private List<Path> fileAddons;

    public SandboxLoader(SandboxAPI api, List<Path> fileAddonsToLoad) {
        this.api = api;
        this.fileAddonsToLoad = fileAddonsToLoad;
    }

    public List<Path> getFileAddons() {
        return fileAddons;
    }

    public void load() throws IOException {
        loader = new AddonClassLoader(getClass().getClassLoader());
        fileAddons = new ArrayList<>();
        ;

        Set<URL> urls = new HashSet<>();
        fileAddonsToLoad.forEach(path -> {
            try {
                urls.add(path.toUri().toURL());
                fileAddons.add(path);
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
                    getClassLoader().addURL(cURL);
                    if (config.contains("main-class")) {
                        Class mainClass = getClassLoader().loadClass(config.get("main-class"));
                        if (!Addon.class.isAssignableFrom(mainClass)) {
                            return;
                        }
                        Addon addon = (Addon) mainClass.getConstructor().newInstance();
                        addon.init(api);
                        addon.register();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(configStream);
                }
            });
        });
    }


    public AddonClassLoader getClassLoader() {
        return loader;
    }
}