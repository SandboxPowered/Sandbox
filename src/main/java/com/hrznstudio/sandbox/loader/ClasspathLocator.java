package com.hrznstudio.sandbox.loader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.function.Consumer;

public class ClasspathLocator implements AddonLocator {
    @Override
    public void find(SandboxLoader loader, Consumer<URL> urlConsumer) {
        try {
            Enumeration<URL> urls = loader.getClassLoader().getResources("sandbox.toml");
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
