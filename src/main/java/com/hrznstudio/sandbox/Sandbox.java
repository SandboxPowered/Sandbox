package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.api.ISandbox;
import com.hrznstudio.sandbox.api.SandboxRegistry;
import com.hrznstudio.sandbox.api.Side;
import com.hrznstudio.sandbox.ragdoll.Ragdolls;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.registry.Registry;

public class Sandbox implements ISandbox {
    public static Sandbox SANDBOX = new Sandbox();

    public static Ragdolls ragdolls = new Ragdolls();

    public static boolean incompatibleModsLoaded = false;

    @Override
    public Side getSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Side.CLIENT : Side.SERVER;
    }

    @Override
    public SandboxRegistry getRegistry(SandboxRegistry.RegistryType registryType) {
        switch (registryType) {
            case BLOCK:
                return (SandboxRegistry) Registry.BLOCK;
            case ITEM:
                return (SandboxRegistry) Registry.ITEM;
            case ENTITY:
                return (SandboxRegistry) Registry.ENTITY_TYPE;
            case BLOCK_ENTITY:
                return (SandboxRegistry) Registry.BLOCK_ENTITY;
            default:
                return null;
        }
    }
}