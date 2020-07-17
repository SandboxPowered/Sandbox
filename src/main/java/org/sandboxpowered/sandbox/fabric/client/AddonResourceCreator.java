package org.sandboxpowered.sandbox.fabric.client;

import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;

import java.util.function.Consumer;

public class AddonResourceCreator implements ResourcePackProvider {

    @Override
    public <T extends ResourcePackProfile> void register(Consumer<T> consumer, ResourcePackProfile.Factory<T> factory) {
//        SandboxServer.INSTANCE.loader.getAddons().forEach(spec -> { TODO
//            try {
//                Path path = Paths.get(spec.getPath().toURI());
//                if (Files.isDirectory(path)) {
//                    consumer.accept(ResourcePackProfile.of(
//                            spec.getTitle(),
//                            true,
//                            () -> new AddonFolderResourcePack(path, spec),
//                            factory,
//                            ResourcePackProfile.InsertionPosition.BOTTOM,
//                            ResourcePackSource.PACK_SOURCE_BUILTIN
//                    ));
//                } else {
//                    consumer.accept(ResourcePackProfile.of(
//                            spec.getTitle(),
//                            true,
//                            () -> new AddonResourcePack(path.toFile()),
//                            factory,
//                            ResourcePackProfile.InsertionPosition.BOTTOM,
//                            ResourcePackSource.PACK_SOURCE_BUILTIN
//                    ));
//                }
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//        });
    }
}