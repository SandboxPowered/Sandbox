package org.sandboxpowered.sandbox.fabric.resources;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import org.sandboxpowered.sandbox.fabric.SandboxResources;

import java.util.Map;

public class SandboxResourceCreator implements ResourcePackProvider {
    @Override
    public <T extends ResourcePackProfile> void register(Map<String, T> var1, ResourcePackProfile.Factory<T> var2) {
        FabricLoader.getInstance().getModContainer("sandbox").map(container -> {
            return ResourcePackProfile.of("Sandbox Resources", true, () -> new SandboxResources(container.getRootPath()), var2, ResourcePackProfile.InsertionPosition.BOTTOM);
        }).ifPresent(t -> var1.put("sandbox", t));
    }
}