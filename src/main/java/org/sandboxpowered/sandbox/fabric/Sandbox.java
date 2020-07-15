package org.sandboxpowered.sandbox.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Pair;
import org.sandboxpowered.api.util.Side;
import org.sandboxpowered.sandbox.fabric.client.SandboxClient;
import org.sandboxpowered.sandbox.fabric.internal.ISandbox;

import java.util.List;

public class Sandbox implements ISandbox {
    public static Sandbox SANDBOX = new Sandbox();

    public static void open(String prefix, List<Pair<String, String>> addons) {
        if (SandboxClient.INSTANCE == null)
            SandboxClient.constructAndSetup();
        SandboxClient.INSTANCE.open(prefix, addons);
    }

    @Override
    public Side getSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Side.CLIENT : Side.SERVER;
    }
}