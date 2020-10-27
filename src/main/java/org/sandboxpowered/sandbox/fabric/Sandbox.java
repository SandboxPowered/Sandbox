package org.sandboxpowered.sandbox.fabric;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Pair;
import org.sandboxpowered.api.util.Side;
import org.sandboxpowered.sandbox.fabric.internal.ISandbox;
import org.sandboxpowered.sandbox.fabric.util.SandboxStorage;

import java.util.Collection;
import java.util.List;

public class Sandbox implements ISandbox {
    public static final String ID = "sandbox";
    public static final Sandbox SANDBOX = new Sandbox();

    @Environment(EnvType.CLIENT)
    public static void openClient(String prefix, List<Pair<String, String>> addons) {
        // No loading currently
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
        if (getSide().isServer())
            reloadServer();
    }

    private void reloadServer() {
        MinecraftServer server = (MinecraftServer) SandboxStorage.getServer();
        ResourcePackManager resourcePackManager = server.getDataPackManager();
        resourcePackManager.scanPacks();
        Collection<String> enabledPacks = Lists.newArrayList(resourcePackManager.getEnabledNames());
        Collection<String> disabledPacks = server.getSaveProperties().getDataPackSettings().getDisabled();
        for (String string : resourcePackManager.getNames()) {
            if (!disabledPacks.contains(string) && !enabledPacks.contains(string)) {
                enabledPacks.add(string);
            }
        }
        server.reloadResources(enabledPacks);
    }

    @Environment(EnvType.CLIENT)
    public void reloadClient() {
        MinecraftClient.getInstance().reloadResources();
        MinecraftClient.getInstance().initializeSearchableContainers();
        if (SandboxStorage.getServer() != null) {
            reloadServer();
        }
    }
}