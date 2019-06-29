package com.hrznstudio.sandbox.fabric;

import com.hrznstudio.sandbox.fabric.api.SandboxRegistry;
import com.hrznstudio.sandbox.fabric.util.Log;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.SlabBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Sandbox implements ModInitializer {
    public static boolean setup() {
        Log.info("Setting up Sandbox environment");
        Block block = new SlabBlock(Block.Settings.of(Material.METAL));
        Registry<Block> registry = Registry.BLOCK;
        Registry.register(registry, "test_block", block);
        block.getStateFactory().getStates().forEach(Block.STATE_IDS::add);
        return true;
    }

    public static void shutdown() {
        ((SandboxRegistry) Registry.BLOCK).remove(new Identifier("test_block"));
    }

    @Override
    public void onInitialize() {
    }
}