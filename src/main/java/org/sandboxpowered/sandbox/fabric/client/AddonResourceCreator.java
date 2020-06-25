package org.sandboxpowered.sandbox.fabric.client;

import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import org.sandboxpowered.sandbox.fabric.server.SandboxServer;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class AddonResourceCreator implements ResourcePackProvider {

    @Override
    public <T extends ResourcePackProfile> void register(Consumer<T> consumer, ResourcePackProfile.Factory<T> factory) {
        SandboxServer.INSTANCE.loader.getAddons().forEach(spec -> {
            try {
                Path path = Paths.get(spec.getPath().toURI());
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
                            () -> new AddonResourcePack(path.toFile()),
                            factory,
                            ResourcePackProfile.InsertionPosition.BOTTOM,
                            ResourcePackSource.PACK_SOURCE_BUILTIN
                    ));
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }
}