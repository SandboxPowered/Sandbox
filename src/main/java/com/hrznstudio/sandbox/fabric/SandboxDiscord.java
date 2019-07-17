package com.hrznstudio.sandbox.fabric;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class SandboxDiscord {

    public static void start() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        DiscordRPC.discordInitialize(
                "596124826087849984",
                eventHandlers,
                false
        );
        DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("Loading")
                .setBigImage("logo", "")
                .build()
        );
    }

    public static void shutdown() {
        DiscordRPC.discordShutdown();
    }

}
