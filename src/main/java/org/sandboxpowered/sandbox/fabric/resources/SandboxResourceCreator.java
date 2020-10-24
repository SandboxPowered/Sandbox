package org.sandboxpowered.sandbox.fabric.resources;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import org.sandboxpowered.sandbox.fabric.Sandbox;
import org.sandboxpowered.sandbox.fabric.SandboxResources;

import java.util.function.Consumer;

public class SandboxResourceCreator implements ResourcePackProvider {
    @Override
    public void register(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory) {
        FabricLoader.getInstance().getModContainer(Sandbox.ID).map(modContainer -> ResourcePackProfile.of(
                "Sandbox Resources",
                true,
                () -> new SandboxResources(modContainer.getRootPath()),
                factory,
                ResourcePackProfile.InsertionPosition.BOTTOM,
                ResourcePackSource.PACK_SOURCE_BUILTIN
        )).ifPresent(consumer);
    }
}