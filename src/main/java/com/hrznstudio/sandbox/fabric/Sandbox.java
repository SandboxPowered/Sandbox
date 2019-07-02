package com.hrznstudio.sandbox.fabric;

import com.eclipsesource.v8.V8ScriptExecutionException;
import com.hrznstudio.sandbox.api.*;
import com.hrznstudio.sandbox.api.addon.AddonInfo;
import com.hrznstudio.sandbox.util.Log;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.FileUtils;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

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
        Log.info("Setting up Sandbox environment");
        ScriptEngine.init(SANDBOX);
        ADDONS = SandboxLoader.locateAddons(SandboxLocation.ADDONS);
        ADDONS.forEach(info -> {
            ACTIVE_ADDON = info;
            File[] s = info.getFolder().getSubFile("blocks").listFiles((dir, name) -> name.endsWith(".js"));
            if (s != null) {
                Stream.of(s).forEach(file -> {
                    try {
                        ScriptEngine.ENGINE.executeVoidScript("'use strict';" +
                                "(function () {" +
                                FileUtils.readFileToString(file, Charset.defaultCharset()) +
                                "\n})();");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch(V8ScriptExecutionException e) {
                        e.printStackTrace();
                    }
                });
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
        for(Identifier identifier : BLOCK_LIST) {
            ((SandboxRegistry) Registry.BLOCK).remove(identifier);
        }
        for(Identifier identifier : ITEM_LIST) {
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