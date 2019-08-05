package com.hrznstudio.sandbox.loader;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.jar.JarFile;

public class DirectoryLocator implements AddonLocator {
    private final Path dir;

    public DirectoryLocator(Path dir) {
        this.dir = dir;
    }

    @Override
    public void find(SandboxLoader loader, Consumer<URL> urlConsumer) {
        if (!Files.exists(dir)) {
            try {
                Files.createDirectory(dir);
                return;
            } catch (Exception e) {
                throw new RuntimeException("Error creating addon directory", e);
            }
        }
        if (!Files.isDirectory(dir)) {
            throw new RuntimeException(String.format("%s is not a directory!", dir));
        }
        try {
            Files.walk(dir, 1)
                    .filter(path -> !Files.isDirectory(path) && path.toString().endsWith(".sbx"))
                    .forEach(path -> {
                        try {
                            JarFile jarFile = new JarFile(path.toFile());
                            if (jarFile.getEntry("sandbox.toml") == null)
                                return;
                            urlConsumer.accept(path.toUri().toURL());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}