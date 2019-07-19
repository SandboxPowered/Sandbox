package com.hrznstudio.sandbox.mixin;

import com.hrznstudio.sandbox.Sandbox;
import com.hrznstudio.sandbox.SandboxClient;
import com.hrznstudio.sandbox.SandboxDiscord;
import com.hrznstudio.sandbox.SandboxServer;
import com.hrznstudio.sandbox.api.ISandboxScreen;
import com.hrznstudio.sandbox.api.Side;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.resource.language.I18n;

public class SandboxHooks {
    public static void shutdown() {
        if (Sandbox.SANDBOX.getSide() == Side.CLIENT) {
            SandboxClient.INSTANCE.shutdown();
            if (SandboxServer.INSTANCE != null && SandboxServer.INSTANCE.isIntegrated())
                SandboxServer.INSTANCE.shutdown();
        } else {
            SandboxServer.INSTANCE.shutdown();
        }
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

    public static boolean setupDedicatedServer() {
        SandboxServer.constructAndSetup(false);
        return true;
    }
}