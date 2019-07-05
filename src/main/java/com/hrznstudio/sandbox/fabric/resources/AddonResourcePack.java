package com.hrznstudio.sandbox.fabric.resources;

import com.google.gson.JsonObject;
import com.hrznstudio.sandbox.api.addon.AddonInfo;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

public class AddonResourcePack implements ResourcePack {
    private final AddonInfo info;
    private final File resources;
    private final File data;

    public AddonResourcePack(AddonInfo info) {
        this.info = info;
        this.resources = info.getFolder().getSubFile("resources");
        this.data = info.getFolder().getSubFile("data");
    }

    @Override
    public InputStream openRoot(String file) throws IOException {
        return new FileInputStream(new File(resources, file));
    }

    @Override
    public InputStream open(ResourceType type, Identifier var2) throws IOException {
        return new FileInputStream(type == ResourceType.CLIENT_RESOURCES ? getResource(var2) : getData(var2));
    }

    @Override
    public Collection<Identifier> findResources(ResourceType var1, String var2, int var3, Predicate<String> var4) {
        return Collections.emptySet();
    }

    @Override
    public boolean contains(ResourceType type, Identifier identifier) {
        return type == ResourceType.CLIENT_RESOURCES ? getResource(identifier).exists() : getData(identifier).exists();
    }

    public File getResource(Identifier identifier) {
        return new File(resources, identifier.getPath());
    }

    public File getData(Identifier identifier) {
        return new File(data, identifier.getPath());
    }

    @Override
    public Set<String> getNamespaces(ResourceType type) {
        return Collections.singleton(info.getId());
    }

    @Override
    public <T> T parseMetadata(ResourceMetadataReader<T> metadataReader) throws IOException {
        return metadataReader.fromJson(new JsonObject());
    }

    @Override
    public String getName() {
        return info.getTitle();
    }

    @Override
    public void close() throws IOException {

    }
}