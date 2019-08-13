package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.api.ISandbox;
import com.hrznstudio.sandbox.api.util.Side;
import com.hrznstudio.sandbox.ragdoll.Ragdolls;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class Sandbox implements ISandbox {
    public static Sandbox SANDBOX = new Sandbox();

    public static Ragdolls ragdolls = new Ragdolls();

    public static boolean unsupportedModsLoaded;

    @Override
    public Side getSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Side.CLIENT : Side.SERVER;
    }
}