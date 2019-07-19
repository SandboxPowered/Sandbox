package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.overlay.AddonLoadingMonitor;
import com.hrznstudio.sandbox.overlay.LoadingOverlay;
import com.hrznstudio.sandbox.util.Log;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.MinecraftClient;

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
        engine.init(Sandbox.SANDBOX);
        //Init client engine
        CONTENT_LIST.clear();
        MinecraftClient.getInstance().setOverlay(new LoadingOverlay(
                MinecraftClient.getInstance(),
                new AddonLoadingMonitor(),
                () -> {
                },
                false
        ));
        Log.info("Setting up Sandbox environment");
        MinecraftClient.getInstance().reloadResourcesConcurrently();
        DiscordRPC.discordUpdatePresence(new DiscordRichPresence.Builder("In Singleplayer")
                .setBigImage("logo", "")
                .build()
        );
    }

    @Override
    public void shutdown() {
        super.shutdown();
        INSTANCE = null;
    }
}