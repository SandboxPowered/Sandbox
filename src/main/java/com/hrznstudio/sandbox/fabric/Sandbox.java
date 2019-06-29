package com.hrznstudio.sandbox.fabric;

import com.hrznstudio.sandbox.api.ISandbox;
import com.hrznstudio.sandbox.api.SandboxRegistry;
import com.hrznstudio.sandbox.api.ScriptEngine;
import com.hrznstudio.sandbox.api.Side;
import com.hrznstudio.sandbox.fabric.util.Log;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Sandbox implements ModInitializer, ISandbox {
    public static Sandbox SANDBOX;

    public Sandbox() {
        SANDBOX = this;
    }

    public static boolean setup() {
        Log.info("Setting up Sandbox environment");
        ScriptEngine.init(SANDBOX);
        Block block = new SlabBlock(Block.Settings.of(Material.METAL));
        Registry<Block> registry = Registry.BLOCK;
        Registry.register(registry, new Identifier("sandbox", "test_block"), block);
        block.getStateFactory().getStates().forEach(Block.STATE_IDS::add);
        return true;
    }

    MinecraftClient

    public static void shutdown() {
        ((SandboxRegistry) Registry.BLOCK).remove(new Identifier("sandbox", "test_block"));
    }

    @Override
    public void onInitialize() {
    }

    @Override
    public Side getSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Side.CLIENT : Side.SERVER;
    }
}