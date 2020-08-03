package org.sandboxpowered.sandbox.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Pair;
import org.sandboxpowered.api.util.Side;
import org.sandboxpowered.sandbox.fabric.client.overlay.LoadingOverlay;
import org.sandboxpowered.sandbox.fabric.internal.ISandbox;

import java.util.List;

public class Sandbox implements ISandbox {
    public static Sandbox SANDBOX = new Sandbox();

    @Environment(EnvType.CLIENT)
    public static void openClient(String prefix, List<Pair<String, String>> addons) {
        MinecraftClient.getInstance().setOverlay(new LoadingOverlay(
                MinecraftClient.getInstance(),
                prefix, addons
        ));
    }

    public static void open(String prefix, List<Pair<String, String>> addons) {
        if (SANDBOX.getSide().isClient())
            openClient(prefix, addons);
    }

    @Override
    public Side getSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Side.CLIENT : Side.SERVER;
    }

    public void reload() {
        if (getSide().isClient())
            reloadClient();
    }

    @Environment(EnvType.CLIENT)
    public void reloadClient() {
        MinecraftClient.getInstance().reloadResourcesConcurrently();
    }
}