package org.sandboxpowered.sandbox.fabric.client;

import com.google.common.collect.Sets;
import org.sandboxpowered.sandbox.api.addon.AddonSpec;
import org.sandboxpowered.sandbox.fabric.util.Log;
import net.minecraft.resource.AbstractFileResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

public class AddonFolderResourcePack extends AbstractFileResourcePack {
    private final Path basePath;
    private final String separator;
    private final AddonSpec spec;

    public AddonFolderResourcePack(Path basePath, AddonSpec spec) {
        super(basePath.toFile());
        this.basePath = basePath.toAbsolutePath().normalize();
        this.separator = basePath.getFileSystem().getSeparator();
        this.spec = spec;
    }

    private Path getPath(String filename) {
        Path childPath = basePath.resolve(filename.replace("/", separator)).toAbsolutePath().normalize();

        if (childPath.startsWith(basePath) && Files.exists(childPath)) {
            return childPath;
        } else {
            return null;
        }
    }

    @Override
    protected InputStream openFile(String var1) throws IOException {
        Path path = getPath(var1);
        if (path != null && Files.isRegularFile(path)) {
            return Files.newInputStream(path);
        }

        throw new FileNotFoundException(var1);
    }

    @Override
    protected boolean containsFile(String filename) {
        Path path = getPath(filename);
        return path != null && Files.isRegularFile(path);
    }

    @Override
    public Collection<Identifier> findResources(ResourceType type, String path, int depth, Predicate<String> predicate) {
        List<Identifier> ids = new ArrayList<>();
        String nioPath = path.replace("/", separator);

        for (String namespace : getNamespaces(type)) {
            Path namespacePath = getPath(type.getName() + "/" + namespace);
            if (namespacePath != null) {
                Path searchPath = namespacePath.resolve(nioPath).toAbsolutePath().normalize();

                if (Files.exists(searchPath)) {
                    try {
                        Files.walk(searchPath, depth)
                                .filter(Files::isRegularFile)
                                .filter((p) -> {
                                    String filename = p.getFileName().toString();
                                    return !filename.endsWith(".mcmeta") && predicate.test(filename);
                                })
                                .map(namespacePath::relativize)
                                .map((p) -> p.toString().replace(separator, "/"))
                                .forEach((s) -> {
                                    try {
                                        ids.add(new Identifier(namespace, s));
                                    } catch (InvalidIdentifierException e) {
                                        Log.error("Encountered an error loading sandbox resources", e);
                                    }
                                });
                    } catch (IOException e) {
                        Log.error("findResources at " + path + " in namespace " + namespace + " failed!", e);
                    }
                }
            }
        }

        return ids;
    }

    @Override
    public Set<String> getNamespaces(ResourceType type) {
        Set<String> namespaces = Sets.newHashSet();
        File baseFile = new File(this.base, type.getName());
        File[] files = baseFile.listFiles((FilenameFilter) DirectoryFileFilter.DIRECTORY);
        if (files != null) {
            for (File file : files) {
                String path = relativize(baseFile, file);
                if (path.equals(path.toLowerCase(Locale.ROOT))) {
                    namespaces.add(path.substring(0, path.length() - 1));
                } else {
                    this.warnNonLowercaseNamespace(path);
                }
            }
        }

        return namespaces;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public String getName() {
        return spec.getTitle() + " Resources";
    }
}