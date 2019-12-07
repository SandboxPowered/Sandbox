package org.sandboxpowered.sandbox.fabric.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Pair;
import org.sandboxpowered.sandbox.api.Registries;
import org.sandboxpowered.sandbox.api.content.Content;
import org.sandboxpowered.sandbox.api.util.Identity;
import org.sandboxpowered.sandbox.api.util.Side;
import org.sandboxpowered.sandbox.fabric.SandboxCommon;
import org.sandboxpowered.sandbox.fabric.client.overlay.LoadingOverlay;
import org.sandboxpowered.sandbox.fabric.event.EventDispatcher;
import org.sandboxpowered.sandbox.fabric.loader.SandboxLoader;
import org.sandboxpowered.sandbox.fabric.server.SandboxServer;
import org.sandboxpowered.sandbox.fabric.util.Log;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class SandboxClient extends SandboxCommon {
    public static SandboxClient INSTANCE;
    public SandboxLoader loader;

    private SandboxClient() {
        INSTANCE = this;
    }

    public static SandboxClient constructAndSetup() {
        SandboxClient client = new SandboxClient();
        client.setup();
        return client;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    protected void setup() {
        //Init client engine
        Log.info("Setting up Clientside Sandbox environment");
    }

    @Override
    public <T extends Content<T>> void register(Identity identity, T content) {
        if (SandboxServer.INSTANCE == null)
            Registries.getRegistry(content.getContentType()).register(identity, content);
    }

    @Override
    public void shutdown() {
        INSTANCE = null;
        if (SandboxServer.INSTANCE == null)
            EventDispatcher.clear();
    }

    public void open(String prefix, List<Pair<String, String>> addons) {
        MinecraftClient.getInstance().setOverlay(new LoadingOverlay(
                MinecraftClient.getInstance(),
                prefix,
                addons
        ));
    }

    public void load(List<Path> addons) {
        loader = new SandboxLoader(this, addons);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MinecraftClient.getInstance().worldRenderer.reload();
        MinecraftClient.getInstance().reloadResourcesConcurrently();
    }
}