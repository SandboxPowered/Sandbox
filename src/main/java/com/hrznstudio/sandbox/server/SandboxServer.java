package com.hrznstudio.sandbox.server;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hrznstudio.sandbox.SandboxCommon;
import com.hrznstudio.sandbox.api.SandboxRegistry;
import com.hrznstudio.sandbox.event.EventDispatcher;
import com.hrznstudio.sandbox.event.mod.ModEvent;
import com.hrznstudio.sandbox.loader.SandboxLoader;
import com.hrznstudio.sandbox.util.Log;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.util.Map;

public class SandboxServer extends SandboxCommon {
    private static final Gson GSON = new GsonBuilder().create();
    public static String[] ARGS;
    public static SandboxServer INSTANCE;
    private final boolean isIntegrated;
    private SandboxLoader loader;
    public final Map<Block, Item> BLOCK_ITEMS = Maps.newHashMap();

    private SandboxServer(boolean isIntegrated) {
        this.isIntegrated = isIntegrated;
        INSTANCE = this;
    }

    public static SandboxServer constructAndSetup(boolean integrated) {
        SandboxServer server = new SandboxServer(integrated);
        server.setup();
        return server;
    }

    @Override
    protected void setup() {
        Log.info("Setting up Serverside Sandbox environment");
        dispatcher = new EventDispatcher();
        Registry.REGISTRIES.stream().map(reg -> (SandboxRegistry.Internal) reg).forEach(SandboxRegistry.Internal::store);
        BLOCK_ITEMS.clear();
        Item.BLOCK_ITEMS.forEach(BLOCK_ITEMS::put);
        load();
        if (!isIntegrated) {
            setupDedicated();
        }
        dispatcher.publish(new ModEvent.Init());
    }

    protected void load() {
        loader = new SandboxLoader();
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void setupDedicated() {

    }

    public boolean isIntegrated() {
        return isIntegrated;
    }

    @Override
    public void shutdown() {
        Registry.REGISTRIES.stream().map(reg -> (SandboxRegistry.Internal) reg).forEach(SandboxRegistry.Internal::reset);
        Item.BLOCK_ITEMS.clear();
        BLOCK_ITEMS.forEach(Item.BLOCK_ITEMS::put);
        INSTANCE = null;
    }
}