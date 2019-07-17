package com.hrznstudio.sandbox.fabric;

import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8ScriptExecutionException;
import com.hrznstudio.sandbox.api.*;
import com.hrznstudio.sandbox.api.addon.AddonInfo;
import com.hrznstudio.sandbox.fabric.api.ISandboxScreen;
import com.hrznstudio.sandbox.fabric.block.JavascriptBlock;
import com.hrznstudio.sandbox.fabric.overlay.AddonLoadingMonitor;
import com.hrznstudio.sandbox.fabric.overlay.LoadingOverlay;
import com.hrznstudio.sandbox.util.FileUtil;
import com.hrznstudio.sandbox.util.Log;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.function.Function;

public class Sandbox implements ISandbox {
    public static Sandbox SANDBOX = new Sandbox();

    public static List<AddonInfo> ADDONS = Collections.emptyList();
    public static AddonInfo ACTIVE_ADDON = null;

    public static Map<Class, List<Identifier>> CONTENT_LIST = new HashMap<>();
    public static Map<SandboxRegistry.RegistryType, Map<String, Function<V8Object, ?>>> REGISTRIES = new HashMap<>();

    static {
        getReg(SandboxRegistry.RegistryType.BLOCK).put("block", JavascriptBlock::new);
    }

    public static List<Identifier> getContentList(Class contentClass) {
        return CONTENT_LIST.computeIfAbsent(contentClass, a -> new ArrayList<>());
    }

    public static Map<String, Function<V8Object, ?>> getReg(SandboxRegistry.RegistryType type) {
        return REGISTRIES.computeIfAbsent(type, aClass -> new HashMap<>());
    }

    public static boolean setup() throws V8ScriptExecutionException {
        CONTENT_LIST.clear();
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

        MinecraftClient.getInstance().reloadResourcesConcurrently();
        DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("In Singleplayer")
                .setBigImage("logo", "")
                .build()
        );
        return true;
    }

    public static void loadBlock(Identifier identifier, Block block, ItemGroup itemGroup) {
        getContentList(Block.class).add(identifier);
        Registry.register(Registry.BLOCK, identifier, block);
        loadItem(identifier, new BlockItem(block, new Item.Settings().group(itemGroup)));
    }

    public static void loadItem(Identifier identifier, Item item) {
        getContentList(Item.class).add(identifier);
        Registry.register(Registry.ITEM, identifier, item);
        if (item instanceof BlockItem)
            Item.BLOCK_ITEMS.put(((BlockItem) item).getBlock(), item);
    }

    public static void shutdown() {
        for (Identifier identifier : getContentList(Block.class)) {
            ((SandboxRegistry) Registry.BLOCK).remove(identifier);
        }
        for (Identifier identifier : getContentList(Item.class)) {
            ((SandboxRegistry) Registry.ITEM).remove(identifier);
        }
        ScriptEngine.shutdown();
    }

    public static void setupGlobal() {
        SandboxDiscord.start();
    }

    public static void shutdownGlobal() {
        SandboxDiscord.shutdown();
    }

    public static Screen openScreen(Screen screen) {
        if (screen instanceof TitleScreen && screen instanceof ISandboxScreen) {
            ((ISandboxScreen) screen).getButtons().removeIf(w -> w.getMessage().equals(I18n.translate("menu.online")));
            DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("In Menu")
                    .setBigImage("logo", "")
                    .build()
            );
        }
        return screen;
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