package org.sandboxpowered.sandbox.fabric.client;

import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import org.sandboxpowered.internal.AddonSpec;
import org.sandboxpowered.sandbox.fabric.loader.SandboxLoader;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class AddonResourceCreator implements ResourcePackProvider {

    @Override
    public void register(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory) {
        if (SandboxLoader.loader == null || SandboxLoader.loader.getFabric() == null)
            return;
        SandboxLoader.loader.getFabric().getAllAddons().forEach((info, addon) -> {
            AddonSpec spec = (AddonSpec) info;
            Path path;
            try {
                path = Paths.get(spec.getPath().toURI());
            } catch (URISyntaxException e) {
                return;
            }
            if (Files.isDirectory(path)) {
                consumer.accept(ResourcePackProfile.of(
                        spec.getTitle(),
                        true,
                        () -> new AddonFolderResourcePack(path, spec),
                        factory,
                        ResourcePackProfile.InsertionPosition.BOTTOM,
                        ResourcePackSource.PACK_SOURCE_BUILTIN
                ));
            } else {
                consumer.accept(ResourcePackProfile.of(
                        spec.getTitle(),
                        true,
                        () -> new AddonResourcePack(path.toFile(), spec),
                        factory,
                        ResourcePackProfile.InsertionPosition.BOTTOM,
                        ResourcePackSource.PACK_SOURCE_BUILTIN
                ));
            }
        });
    }
}