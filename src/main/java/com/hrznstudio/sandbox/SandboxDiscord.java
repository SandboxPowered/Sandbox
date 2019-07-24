package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.util.Log;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.apache.commons.codec.binary.Base64;

public class SandboxDiscord {

    private static Thread callbackThread;

    public static void start() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.joinGame = joinSecret -> {
            String[] secret = new String(Base64.decodeBase64(joinSecret)).split(" ");
            String ip = secret[0];
            String port = secret[1];
            String password = secret.length > 2 ? secret[2] : "";
            MinecraftClient.getInstance().openScreen(new ConnectScreen(new TitleScreen(), MinecraftClient.getInstance(), ip, Integer.parseInt(port)));
        };
        eventHandlers.errored = (errorCode, message) -> Log.error(String.format("Discord integration encountered an error '%d', %s", errorCode, message));
        eventHandlers.disconnected = (errorCode, message) -> Log.error(String.format("Discord unexpectedly disconnected '%d', %s", errorCode, message));
        eventHandlers.ready = user -> Log.info(String.format("Successfully connected to Discord as %s#%s", user.username, user.discriminator));
        eventHandlers.joinRequest = request -> DiscordRPC.discordRespond(request.userId, DiscordRPC.DiscordReply.YES);
        DiscordRPC.discordInitialize(
                "596124826087849984",
                eventHandlers,
                false
        );
        DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("Loading")
                .setBigImage("logo", "")
                .build()
        );
        callbackThread = new Thread(() -> {
            while (!callbackThread.isInterrupted()) {
                DiscordRPC.discordRunCallbacks();
            }
        }, "Discord-Callbacks");
        callbackThread.start();
    }

    public static void shutdown() {
        callbackThread.interrupt();
        DiscordRPC.discordShutdown();
    }

}
