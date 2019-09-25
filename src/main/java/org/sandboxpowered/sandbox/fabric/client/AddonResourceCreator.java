package org.sandboxpowered.sandbox.fabric.client;

import net.minecraft.resource.ResourcePackContainer;
import net.minecraft.resource.ResourcePackCreator;
import org.sandboxpowered.sandbox.fabric.server.SandboxServer;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class AddonResourceCreator implements ResourcePackCreator {
    @Override
    public <T extends ResourcePackContainer> void registerContainer(Map<String, T> packs, ResourcePackContainer.Factory<T> var2) {
        SandboxServer.INSTANCE.loader.getAddons().forEach(spec -> {
            try {
                Path path = Paths.get(spec.getPath().toURI());
                if (Files.isDirectory(path))
                    packs.put(spec.getModid(), ResourcePackContainer.of(spec.getTitle(), true, () -> new AddonFolderResourcePack(path, spec), var2, ResourcePackContainer.InsertionPosition.BOTTOM));
                else
                    packs.put(spec.getModid(), ResourcePackContainer.of(spec.getTitle(), true, () -> new AddonResourcePack(path.toFile()), var2, ResourcePackContainer.InsertionPosition.BOTTOM));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }
}