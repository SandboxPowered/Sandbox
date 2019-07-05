package com.hrznstudio.sandbox.fabric.resources;

import com.hrznstudio.sandbox.fabric.SandboxResources;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackContainer;
import net.minecraft.resource.ResourcePackCreator;

import java.util.Map;

public class SandboxResourceCreator implements ResourcePackCreator {
    @Override
    public <T extends ResourcePackContainer> void registerContainer(Map<String, T> var1, ResourcePackContainer.Factory<T> var2) {
        FabricLoader.getInstance().getModContainer("sandbox").map(container -> {
            return ResourcePackContainer.of("Sandbox Resources", true, () -> new SandboxResources(container.getRootPath()), var2, ResourcePackContainer.InsertionPosition.BOTTOM);
        }).ifPresent(t -> var1.put("sandbox", t));
    }
}