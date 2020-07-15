package org.sandboxpowered.sandbox.fabric.resources;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import org.sandboxpowered.sandbox.fabric.SandboxResources;

import java.util.Map;
import java.util.function.Consumer;

public class SandboxResourceCreator implements ResourcePackProvider {
    @Override
    public <T extends ResourcePackProfile> void register(Consumer<T> consumer, ResourcePackProfile.Factory<T> factory) {
        FabricLoader.getInstance().getModContainer("sandbox").map(modContainer -> ResourcePackProfile.of(
                "Sandbox Resources",
                true,
                () -> new SandboxResources(modContainer.getRootPath()),
                factory,
                ResourcePackProfile.InsertionPosition.BOTTOM,
                ResourcePackSource.PACK_SOURCE_BUILTIN
        )).ifPresent(consumer);
    }
}