package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.api.ISandbox;
import com.hrznstudio.sandbox.api.util.Side;
import com.hrznstudio.sandbox.client.SandboxClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Pair;

import java.util.List;

public class Sandbox implements ISandbox {
    public static Sandbox SANDBOX = new Sandbox();

//    public static Ragdolls ragdolls = new Ragdolls();

    public static boolean unsupportedModsLoaded;

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