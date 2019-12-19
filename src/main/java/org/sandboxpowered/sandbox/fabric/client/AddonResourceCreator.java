package org.sandboxpowered.sandbox.fabric.client;

import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import org.sandboxpowered.sandbox.fabric.server.SandboxServer;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class AddonResourceCreator implements ResourcePackProvider {
    @Override
    public <T extends ResourcePackProfile> void register(Map<String, T> packs, ResourcePackProfile.Factory<T> var2) {
        SandboxServer.INSTANCE.loader.getAddons().forEach(spec -> {
            try {
                Path path = Paths.get(spec.getPath().toURI());
                if (Files.isDirectory(path))
                    packs.put(spec.getModid(), ResourcePackProfile.of(spec.getTitle(), true, () -> new AddonFolderResourcePack(path, spec), var2, ResourcePackProfile.InsertionPosition.BOTTOM));
                else
                    packs.put(spec.getModid(), ResourcePackProfile.of(spec.getTitle(), true, () -> new AddonResourcePack(path.toFile()), var2, ResourcePackProfile.InsertionPosition.BOTTOM));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }
}