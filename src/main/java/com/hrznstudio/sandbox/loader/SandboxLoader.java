package com.hrznstudio.sandbox.loader;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import com.hrznstudio.sandbox.api.IAddon;
import com.hrznstudio.sandbox.client.SandboxClient;
import com.hrznstudio.sandbox.security.AddonClassLoader;
import com.hrznstudio.sandbox.server.SandboxServer;
import net.fabricmc.loader.util.UrlUtil;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class SandboxLoader {

    private AddonClassLoader loader;

    public static void main(String[] args) throws IOException {
        new SandboxLoader().load();
    }

    public void load() throws IOException {
        loader = new AddonClassLoader(getClass().getClassLoader());

        Set<URL> urls = new HashSet<>();
        Files.walk(Paths.get("addons"), 1)
                .filter(path -> path.toString().endsWith(".sbx"))
                .forEach(path -> {
                    try {
                        urls.add(path.toUri().toURL());
                    } catch (Exception e) {
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
                    if (!IAddon.class.isAssignableFrom(mainClass)) {
                        return;
                    }
                    IAddon addon = (IAddon) mainClass.getConstructor().newInstance();
                    addon.initServer(SandboxServer.INSTANCE);
                    addon.initClient(SandboxClient.INSTANCE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(configStream);
            }
        });
    }


    public AddonClassLoader getClassLoader() {
        return loader;
    }
}