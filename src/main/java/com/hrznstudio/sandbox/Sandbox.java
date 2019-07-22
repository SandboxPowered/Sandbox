package com.hrznstudio.sandbox;

import com.eclipsesource.v8.V8Object;
import com.hrznstudio.sandbox.api.ISandbox;
import com.hrznstudio.sandbox.api.SandboxRegistry;
import com.hrznstudio.sandbox.api.Side;
import com.hrznstudio.sandbox.block.JavascriptBlock;
import com.hrznstudio.sandbox.block.JavascriptSlabBlock;
import com.hrznstudio.sandbox.ragdoll.Ragdolls;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Sandbox implements ISandbox {
    public static Sandbox SANDBOX = new Sandbox();

    public static Ragdolls ragdolls = new Ragdolls();

    public static Map<SandboxRegistry.RegistryType, Map<String, Function<V8Object, ?>>> REGISTRIES = new HashMap<>();

    static {
        getReg(SandboxRegistry.RegistryType.BLOCK).put("block", JavascriptBlock::new);
        getReg(SandboxRegistry.RegistryType.BLOCK).put("slab", JavascriptSlabBlock::new);
    }

    public static Map<String, Function<V8Object, ?>> getReg(SandboxRegistry.RegistryType type) {
        return REGISTRIES.computeIfAbsent(type, aClass -> new HashMap<>());
    }

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