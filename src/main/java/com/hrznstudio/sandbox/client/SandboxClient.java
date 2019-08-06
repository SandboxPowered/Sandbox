package com.hrznstudio.sandbox.client;

import com.hrznstudio.sandbox.SandboxCommon;
import com.hrznstudio.sandbox.event.EventDispatcher;
import com.hrznstudio.sandbox.util.Log;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.MinecraftClient;
import reactor.core.publisher.EmitterProcessor;

public class SandboxClient extends SandboxCommon {
    public static SandboxClient INSTANCE;

    private SandboxClient() {
        INSTANCE = this;
    }

    public static SandboxClient constructAndSetup() {
        SandboxClient client = new SandboxClient();
        client.setup();
        return client;
    }

    @Override
    protected void setup() {
        CONTENT_LIST.clear();
        //Init client engine
//        MinecraftClient.getInstance().setOverlay(new LoadingOverlay(
//                MinecraftClient.getInstance(),
//                new AddonLoadingMonitor(),
//                () -> {
//                },
//                false
//        ));
        Log.info("Setting up Clientside Sandbox environment");
        dispatcher = new EventDispatcher(
                EmitterProcessor.create()
        );
        MinecraftClient.getInstance().reloadResourcesConcurrently();
        DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("In Private Session")
                .setBigImage("gm_debug", String.format("Playing %s", "Debug"))
                .setSecrets("wah", "")
                .setParty("wah2", 5, 12)
                .setStartTimestamps(System.currentTimeMillis() / 1000)
                .setDetails("Playing on 'world'")
                .build()
        );
    }

    @Override
    public void shutdown() {
        super.shutdown();
        INSTANCE = null;
    }
}