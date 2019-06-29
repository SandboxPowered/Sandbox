package com.hrznstudio.sandbox.fabric.mixin;

import com.hrznstudio.sandbox.fabric.Sandbox;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {
    @Inject(method = "shutdown",
            at = @At(value = "TAIL")
    )
    public void shutdown(CallbackInfo info) {
        Sandbox.shutdown();
    }
}