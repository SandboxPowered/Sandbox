package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.api.Side;
import com.hrznstudio.sandbox.client.DownloadScreen;
import com.hrznstudio.sandbox.client.SandboxClient;
import com.hrznstudio.sandbox.security.AddonSecurityPolicy;
import com.hrznstudio.sandbox.server.SandboxServer;
import com.hrznstudio.sandbox.util.ArrayUtil;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;

import java.security.Policy;

public class SandboxHooks {
    public static void shutdown() {
        if (Sandbox.SANDBOX.getSide() == Side.CLIENT) {
            MinecraftClient.getInstance().execute(SandboxClient.INSTANCE::shutdown);
            if (SandboxServer.INSTANCE != null && SandboxServer.INSTANCE.isIntegrated())
                SandboxServer.INSTANCE.shutdown();
        } else {
            SandboxServer.INSTANCE.shutdown();
        }
    }

    public static void setupGlobal() {
        Policy.setPolicy(new AddonSecurityPolicy());
        System.setSecurityManager(new SecurityManager());
        if (FabricLoader.getInstance().getAllMods()
                .stream()
                .map(ModContainer::getMetadata)
                .map(ModMetadata::getId)
                .anyMatch(id -> !id.equals("sandbox") && !id.equals("fabricloader"))) {
            throw new RuntimeException("Incompatible Mods Loaded");
        }
        SandboxDiscord.start();
    }

    public static void shutdownGlobal() {
        SandboxDiscord.shutdown();
    }

    public static Screen openScreen(Screen screen) {
        if (screen instanceof TitleScreen) {
            DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("In Menu")
                    .setBigImage("logo", "")
                    .build()
            );
        }
        if (screen instanceof MultiplayerScreen) {
            screen = new DownloadScreen();
        }
        if (SandboxClient.INSTANCE != null) {
            // TODO: OpenScreenEvent
        }

        return screen;
    }

    public static String[] startDedicatedServer(String[] args) {
        SandboxServer.ARGS = args;
        ArrayUtil.removeAll(args, "-noaddons");
        return args;
    }
}