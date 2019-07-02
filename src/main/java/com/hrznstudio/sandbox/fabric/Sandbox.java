package com.hrznstudio.sandbox.fabric;

import com.eclipsesource.v8.V8ScriptExecutionException;
import com.hrznstudio.sandbox.api.*;
import com.hrznstudio.sandbox.api.addon.AddonInfo;
import com.hrznstudio.sandbox.fabric.overlay.AddonLoadingMonitor;
import com.hrznstudio.sandbox.fabric.overlay.LoadingOverlay;
import com.hrznstudio.sandbox.util.FileUtil;
import com.hrznstudio.sandbox.util.Log;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sandbox implements ModInitializer, ISandbox {
    public static Sandbox SANDBOX;

    public static List<AddonInfo> ADDONS = Collections.emptyList();
    public static AddonInfo ACTIVE_ADDON = null;

    public static List<Identifier> BLOCK_LIST = new ArrayList<>();

    public static List<Identifier> ITEM_LIST = new ArrayList<>();

    public Sandbox() {
        SANDBOX = this;
    }

    public static boolean setup() throws V8ScriptExecutionException {
        MinecraftClient.getInstance().setOverlay(new LoadingOverlay(
                MinecraftClient.getInstance(),
                new AddonLoadingMonitor(),
                () -> {
                },
                false
        ));
        Log.info("Setting up Sandbox environment");
        ScriptEngine.init(SANDBOX);
        ADDONS = SandboxLoader.locateAddons(SandboxLocation.ADDONS);
        ADDONS.forEach(info -> {
            ACTIVE_ADDON = info;
            AddonInfo.FolderStructure scripts = info.getFolder().getSubFolder("scripts");
            for (RegistryOrder order : RegistryOrder.values()) {
                FileUtil.getFiles(
                        scripts.getSubFile(order.getFolder()),
                        (dir, name) -> name.endsWith(".js"),
                        true
                ).forEach(file -> ScriptEngine.executeScript(file).ifPresent(e -> Log.error("Script encountered an error", e.getException())));
            }
            ACTIVE_ADDON = null;
        });

        /*Block block = new SlabBlock(Block.Settings.copy(Blocks.GOLD_BLOCK));
        Registry.register(Registry.BLOCK, new Identifier("test", "test_block"), block);
        ((SandboxRegistry) Registry.ITEM).register(new Identifier("test", "test_block"), new BlockItem(block, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        */
        MinecraftClient.getInstance().reloadResourcesConcurrently();
        return true;
    }

    public static void loadBlock(Identifier identifier, String namespace, Block block, ItemGroup itemGroup) {
        BLOCK_LIST.add(identifier);
        Registry.register(Registry.BLOCK, identifier, block);
        ((SandboxRegistry) Registry.ITEM).register(identifier, new BlockItem(block, new Item.Settings().group(itemGroup)));
    }

    public static void shutdown() {
        for (Identifier identifier : BLOCK_LIST) {
            ((SandboxRegistry) Registry.BLOCK).remove(identifier);
        }
        for (Identifier identifier : ITEM_LIST) {
            ((SandboxRegistry) Registry.ITEM).remove(identifier);
        }
        /*((SandboxRegistry) Registry.BLOCK).remove(new Identifier("test", "test_block"));
        ((SandboxRegistry) Registry.ITEM).remove(new Identifier("test", "test_block"));*/
        ScriptEngine.shutdown();
    }

    @Override
    public void onInitialize() {

    }

    @Override
    public Side getSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Side.CLIENT : Side.SERVER;
    }

    @Override
    public SandboxRegistry getRegistry(String registryString) {
        switch (registryString) {
            case "block":
                return (SandboxRegistry) Registry.BLOCK;
            case "item":
                return (SandboxRegistry) Registry.ITEM;
            case "entity":
                return (SandboxRegistry) Registry.ENTITY_TYPE;
            case "tile":
                return (SandboxRegistry) Registry.BLOCK_ENTITY;
            default:
                return null;
        }
    }
}