package com.hrznstudio.sandbox.mixin;

import com.hrznstudio.sandbox.SandboxHooks;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {
    @ModifyVariable(method = "main", at = @At("HEAD"), ordinal = 0)
    private static String[] main(String[] args) {
        return SandboxHooks.startDedicatedServer(args);
    }

    @Inject(method = "shutdown",
            at = @At(value = "TAIL")
    )
    public void shutdown(CallbackInfo info) {
        SandboxHooks.shutdown();
    }
}