package com.hrznstudio.sandbox.server;

import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hrznstudio.sandbox.SandboxCommon;
import com.hrznstudio.sandbox.api.SandboxInternal;
import com.hrznstudio.sandbox.api.util.Side;
import com.hrznstudio.sandbox.event.EventDispatcher;
import com.hrznstudio.sandbox.loader.SandboxLoader;
import com.hrznstudio.sandbox.network.AddonS2CPacket;
import com.hrznstudio.sandbox.network.Packet;
import com.hrznstudio.sandbox.util.Log;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.Pair;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SandboxServer extends SandboxCommon {
    private static final Gson GSON = new GsonBuilder().create();
    public static String[] ARGS;
    public static SandboxServer INSTANCE;
    public final Map<Block, Item> BLOCK_ITEMS = Maps.newHashMap();
    private final boolean isIntegrated;
    private SandboxLoader loader;
    private MinecraftServer server;

    private SandboxServer(MinecraftServer server) {
        this.isIntegrated = !(server instanceof DedicatedServer);
        this.server = server;
        INSTANCE = this;
    }

    public static SandboxServer constructAndSetup(MinecraftServer s) {
        SandboxServer server = new SandboxServer(s);
        server.setup();
        return server;
    }

    public Packet createAddonSyncPacket() {
        if (loader.getFileAddons().isEmpty())
            return new AddonS2CPacket(0, "https://cdn.hrzn.studio/sandbox/addons/", Collections.emptyList());
        List<Pair<String, String>> s = new ArrayList<>();
        loader.getFileAddons().forEach(path -> {
            try {
                String hash = Files.hash(path.toFile(), Hashing.farmHashFingerprint64()).toString();
                s.add(new Pair<>(hash + ".sbx", hash));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return new AddonS2CPacket(s.size(), "https://cdn.hrzn.studio/sandbox/addons/", s);
    }

    @Override
    public Side getSide() {
        return Side.SERVER;
    }

    @Override
    protected void setup() {
        Log.info("Setting up Serverside Sandbox environment");
        dispatcher = new EventDispatcher();
        net.minecraft.util.registry.Registry.REGISTRIES.stream().map(reg -> (SandboxInternal.Registry) reg).forEach(SandboxInternal.Registry::store);
        BLOCK_ITEMS.clear();
        Item.BLOCK_ITEMS.forEach(BLOCK_ITEMS::put);
        load();
        if (!isIntegrated) {
            setupDedicated();
        }
    }

    protected void load() {
        List<Path> fileAddons = new ArrayList<>();
        try {
            java.nio.file.Files.walk(Paths.get("addons"), 1)
                    .filter(path -> path.toString().endsWith(".sbx"))
                    .forEach(path -> {
                        try {
                            fileAddons.add(path);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        loader = new SandboxLoader(this, fileAddons);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Path uploadDir = Paths.get("upload");
        try {
            java.nio.file.Files.delete(uploadDir);
            loader.getFileAddons().forEach(path -> {
                try {
                    String hash = Files.hash(path.toFile(), Hashing.farmHashFingerprint64()).toString();
                    FileUtils.copyFile(path.toFile(), new File(uploadDir.toFile(), hash + ".sbx"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
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
        net.minecraft.util.registry.Registry.REGISTRIES.stream().map(reg -> (SandboxInternal.Registry) reg).forEach(SandboxInternal.Registry::reset);
        Item.BLOCK_ITEMS.clear();
        BLOCK_ITEMS.forEach(Item.BLOCK_ITEMS::put);
        INSTANCE = null;
    }

    public MinecraftServer getServer() {
        return server;
    }
}