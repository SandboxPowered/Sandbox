package org.sandboxpowered.loader.packs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.ResourcePackFileNotFoundException;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MergedSandboxPack extends AbstractPackResources {
    private final List<PackResources> delegates;
    private final Map<String, List<PackResources>> namespacesAssets;
    private final Map<String, List<PackResources>> namespacesData;
    private final String name;

    public MergedSandboxPack(String id, String name, List<? extends PackResources> packs) {
        super(new File(id));
        this.delegates = ImmutableList.copyOf(packs);
        this.name = name;
        namespacesAssets = createNamespaceTypeMap(PackType.CLIENT_RESOURCES, delegates);
        namespacesData = createNamespaceTypeMap(PackType.SERVER_DATA, delegates);
    }

    private Map<String, List<PackResources>> createNamespaceTypeMap(PackType type, List<PackResources> packList) {
        Map<String, List<PackResources>> map = new HashMap<>();

        for (PackResources pack : packList) {
            for (String namespace : pack.getNamespaces(type)) {
                map.computeIfAbsent(namespace, (k) -> new ArrayList<>()).add(pack);
            }
        }

        map.replaceAll((k, list) -> ImmutableList.copyOf(list));
        return ImmutableMap.copyOf(map);
    }

    @Override
    protected InputStream getResource(String string) throws IOException {
        throw new ResourcePackFileNotFoundException(this.file, string);
    }

    @Override
    public InputStream getRootResource(String string) throws IOException {
        throw new ResourcePackFileNotFoundException(this.file, string);
    }

    @Override
    protected boolean hasResource(String string) {
        return false;
    }

    @Override
    public boolean hasResource(PackType packType, ResourceLocation resourceLocation) {
        for (PackResources pack : this.getPackSubset(packType, resourceLocation)) {
            if (pack.hasResource(packType, resourceLocation))
                return true;
        }
        return false;
    }

    private static final Gson GSON = new Gson();

    public InputStream getPackMeta() {
        JsonObject meta = new JsonObject();
        JsonObject pack = new JsonObject();
        pack.addProperty("pack_format", SharedConstants.getCurrentVersion().getPackVersion());
        pack.addProperty("description", String.format("Sandbox addon resources (%d)", delegates.size()));
        meta.add("pack", pack);
        return new ByteArrayInputStream(GSON.toJson(meta).getBytes(StandardCharsets.UTF_8));
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializer) throws IOException {
        if (metadataSectionSerializer.getMetadataSectionName().equals("pack")) {
            InputStream inputStream = getPackMeta();
            Throwable error = null;
            try {
                return AbstractPackResources.getMetadataFromStream(metadataSectionSerializer, inputStream);
            } catch (Throwable throwable) {
                error = throwable;
                throw throwable;
            } finally {
                if (inputStream != null) {
                    if (error != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable suppressed) {
                            error.addSuppressed(suppressed);
                        }
                    } else {
                        inputStream.close();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public InputStream getResource(PackType packType, ResourceLocation resourceLocation) throws IOException {
        for (PackResources pack : this.getPackSubset(packType, resourceLocation)) {
            if (pack.hasResource(packType, resourceLocation))
                return pack.getResource(packType, resourceLocation);
        }
        throw new ResourcePackFileNotFoundException(this.file, getFullPath(packType, resourceLocation));
    }

    private static String getFullPath(PackType type, ResourceLocation location) {
        return String.format("%s/%s/%s", type.getDirectory(), location.getNamespace(), location.getPath());
    }

    @Override
    public Collection<ResourceLocation> getResources(PackType type, String pathIn, String pathIn2, int maxDepth, Predicate<String> filter) {
        return this.delegates.stream()
                .flatMap((pack) -> pack.getResources(type, pathIn, pathIn2, maxDepth, filter).stream())
                .collect(Collectors.toList());
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return getNamespaceMap(type).keySet();
    }

    private List<PackResources> getPackSubset(PackType type, ResourceLocation location) {
        List<PackResources> packsWithNamespace = getNamespaceMap(type).get(location.getNamespace());
        return packsWithNamespace == null ? Collections.emptyList() : packsWithNamespace;
    }

    public Map<String, List<PackResources>> getNamespaceMap(PackType type) {
        return type == PackType.CLIENT_RESOURCES ? this.namespacesAssets : this.namespacesData;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void close() {
        delegates.forEach(PackResources::close);
    }
}
