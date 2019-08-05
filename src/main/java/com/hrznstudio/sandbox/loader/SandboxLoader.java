package com.hrznstudio.sandbox.loader;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import com.hrznstudio.sandbox.security.AddonClassLoader;
import net.fabricmc.loader.util.UrlUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class SandboxLoader {

    private AddonClassLoader loader;

    public static void main(String[] args) throws IOException {
        new SandboxLoader().load();
    }

    public void load() throws IOException {
        //TODO: Load from external files
        loader = new AddonClassLoader(getClass().getClassLoader());
        TomlParser parser = new TomlParser();
        Enumeration<URL> enumeration = getClass().getClassLoader().getResources("sandbox.toml");
        Set<URL> urls = new HashSet<>();
        while (enumeration.hasMoreElements()) { // Add it all to a set to temporarily remove duplicates
            urls.add(enumeration.nextElement());
        }

        urls.forEach(cURL -> {
            try {
                URL url = UrlUtil.getSource("sandbox.toml", cURL);
                getClassLoader().addURL(url);
                Config config = parser.parse(cURL);
                Class mainClass = getClassLoader().loadClass(config.get("main-class"));
                mainClass.getConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public AddonClassLoader getClassLoader() {
        return loader;
    }
}