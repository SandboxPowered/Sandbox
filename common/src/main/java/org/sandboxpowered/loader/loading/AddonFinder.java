package org.sandboxpowered.loader.loading;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface AddonFinder {
    String SANDBOX_TOML = "sandbox.toml";

    Collection<URL> findAddons() throws IOException;

    class FolderScanner implements AddonFinder {
        private final Path addonPath;

        public FolderScanner(Path path) {
            this.addonPath = path;
        }

        @Override
        public Collection<URL> findAddons() throws IOException {
            if (Files.notExists(addonPath)) Files.createDirectories(addonPath);
            Set<URL> addonUrls;
            try (Stream<Path> stream = Files.walk(addonPath, 1)) {
                return stream.filter(path -> path.toString().endsWith(".jar"))
                        .map(path -> {
                            try {
                                return path.toUri().toURL();
                            } catch (MalformedURLException e) {
                                throw new RuntimeException(e);
                            }
                        }).collect(Collectors.toSet());
            }
        }
    }

    class ClasspathScanner implements AddonFinder {
        @Override
        public Collection<URL> findAddons() throws IOException {
            Set<URL> addons = new HashSet<>();
            Enumeration<URL> enumeration = getClass().getClassLoader().getResources(SANDBOX_TOML);
            while (enumeration.hasMoreElements()) {
                URL url = getSource(SANDBOX_TOML, enumeration.nextElement());
                addons.add(url);
            }
            return addons;
        }

        public static URL getSource(String filename, URL resourceURL) {
            try {
                URLConnection connection = resourceURL.openConnection();
                if (connection instanceof JarURLConnection) {
                    return ((JarURLConnection) connection).getJarFileURL();
                } else {
                    String path = resourceURL.getPath();
                    if (!path.endsWith(filename)) {
                        throw new RuntimeException(String.format("Could not find code source for file '%s' and URL '%s'!", filename, resourceURL));
                    }

                    return new URL(resourceURL.getProtocol(), resourceURL.getHost(), resourceURL.getPort(), path.substring(0, path.length() - filename.length()));
                }
            } catch (Exception var5) {
                throw new RuntimeException(var5);
            }
        }
    }
}