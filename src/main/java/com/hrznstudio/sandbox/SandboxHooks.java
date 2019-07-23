package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.api.ISandboxScreen;
import com.hrznstudio.sandbox.api.Side;
import com.hrznstudio.sandbox.client.SandboxClient;
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
import net.minecraft.client.resource.language.I18n;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

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
        System.out.println(SandboxHooks.class.getClassLoader().getClass());
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
        if (screen instanceof TitleScreen && screen instanceof ISandboxScreen) {
            ((ISandboxScreen) screen).getButtons().removeIf(w -> w.getMessage().equals(I18n.translate("menu.online")));
            DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("In Menu")
                    .setBigImage("logo", "")
                    .build()
            );
        } else {
            if (screen instanceof MultiplayerScreen) {
                return new com.hrznstudio.sandbox.client.MultiplayerScreen();
            }
        }
        return screen;
    }

    public static String[] startDedicatedServer(String[] args) {
        SandboxServer.ARGS = args;
        ArrayUtil.removeAll(args, "-noaddons");
        return args;
    }
}