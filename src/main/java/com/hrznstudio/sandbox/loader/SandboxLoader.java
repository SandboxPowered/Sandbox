package com.hrznstudio.sandbox.loader;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;
import com.hrznstudio.sandbox.security.AddonClassLoader;
import net.fabricmc.loader.util.UrlUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class SandboxLoader {

    private AddonClassLoader loader;

    public static void main(String[] args) throws IOException {
        new SandboxLoader().load();
    }

    public void load() throws IOException {
        //TODO: Load from external files
        loader = new AddonClassLoader();
        TomlParser parser = new TomlParser();
        Enumeration<URL> urls = getClass().getClassLoader().getResources("sandbox.toml");
        while (urls.hasMoreElements()) {
            try {
                URL cURL = urls.nextElement();
                URL url = UrlUtil.getSource("sandbox.toml", cURL);
                getClassLoader().addURL(url);
                Config config = parser.parse(cURL);
                String mainClass = config.get("main-class");
                Class c = getClassLoader().loadClass(mainClass);
                Object obj = c.getConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public AddonClassLoader getClassLoader() {
        return loader;
    }
}