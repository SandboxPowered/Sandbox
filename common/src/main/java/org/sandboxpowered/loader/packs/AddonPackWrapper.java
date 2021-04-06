package org.sandboxpowered.loader.packs;

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
import org.sandboxpowered.internal.AddonSpec;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

public class AddonPackWrapper implements PackResources {
    private static final Gson GSON = new Gson();
    private final AddonSpec spec;
    private final PackResources resources;

    public AddonPackWrapper(AddonSpec spec, PackResources resources) {
        this.spec = spec;
        this.resources = resources;
    }

    @Override
    public InputStream getRootResource(String string) throws IOException {
        try {
            return resources.getRootResource(string);
        } catch (ResourcePackFileNotFoundException e) {
            if ("pack.mcmeta".equals(string)) {
                JsonObject meta = new JsonObject();
                JsonObject pack = new JsonObject();
                pack.addProperty("pack_format", SharedConstants.getCurrentVersion().getPackVersion());
                pack.addProperty("description", spec.getDescription());
                meta.add("pack", pack);
                return new ByteArrayInputStream(GSON.toJson(meta).getBytes(StandardCharsets.UTF_8));
            }
            throw e;
        }
    }

    @Override
    public InputStream getResource(PackType packType, ResourceLocation resourceLocation) throws IOException {
        return resources.getResource(packType, resourceLocation);
    }

    @Override
    public Collection<ResourceLocation> getResources(PackType packType, String string, String string2, int i, Predicate<String> predicate) {
        return resources.getResources(packType, string, string2, i, predicate);
    }

    @Override
    public boolean hasResource(PackType packType, ResourceLocation resourceLocation) {
        return resources.hasResource(packType, resourceLocation);
    }

    @Override
    public Set<String> getNamespaces(PackType packType) {
        return resources.getNamespaces(packType);
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializer) throws IOException {
        InputStream inputStream = this.getRootResource("pack.mcmeta");
        Throwable error = null;

        T metadata;
        try {
            metadata = AbstractPackResources.getMetadataFromStream(metadataSectionSerializer, inputStream);
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

        return metadata;
    }

    @Override
    public String getName() {
        return spec.getTitle();
    }

    @Override
    public void close() {
        resources.close();
    }
}
