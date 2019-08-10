package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.api.Side;
import com.hrznstudio.sandbox.client.DownloadScreen;
import com.hrznstudio.sandbox.client.SandboxClient;
import com.hrznstudio.sandbox.client.SandboxTitleScreen;
import com.hrznstudio.sandbox.event.client.ScreenEvent;
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
        if (FabricLoader.getInstance().getAllMods()
                .stream()
                .map(ModContainer::getMetadata)
                .map(ModMetadata::getId)
                .anyMatch(id -> !id.equals("sandbox") && !id.equals("fabricloader"))) {
            Sandbox.unsupportedModsLoaded = true;
        }
        Policy.setPolicy(new AddonSecurityPolicy());
        SandboxDiscord.start();
    }

    public static void shutdownGlobal() {
        SandboxDiscord.shutdown();
    }

    public static Screen openScreen(Screen screen) {
        if (screen instanceof TitleScreen || (screen == null && MinecraftClient.getInstance().world == null)) {
            DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("In Menu")
                    .setBigImage("logo", "")
                    .build()
            );
            screen = new SandboxTitleScreen();
        }
        if (screen instanceof MultiplayerScreen) {
            screen = new DownloadScreen();
        }
        if (SandboxClient.INSTANCE != null && screen != null) {
            ScreenEvent.Open event = SandboxClient.INSTANCE.getDispatcher().publish(new ScreenEvent.Open(screen));
            if (event.wasCancelled()) {
                screen = MinecraftClient.getInstance().currentScreen;
            } else {
                screen = event.getScreen();
            }
        } else if (SandboxClient.INSTANCE != null) {
            Screen currentScreen = MinecraftClient.getInstance().currentScreen;
            ScreenEvent.Close event = SandboxClient.INSTANCE.getDispatcher().publish(new ScreenEvent.Close(currentScreen, screen));
            if (event.wasCancelled()) {
                screen = currentScreen;
            } else {
                screen = event.getNewScreen();
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