package org.sandboxpowered.loader.fabric.mixin.loading;

import net.minecraft.client.server.IntegratedServer;
import org.sandboxpowered.loader.fabric.SandboxFabric;
import org.sandboxpowered.loader.platform.SandboxPlatform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

@Mixin(IntegratedServer.class)
public class MixinIntegratedServer {

    @Inject(method = "initServer",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/server/IntegratedServer;loadLevel()V",
                    shift = At.Shift.BEFORE),
            cancellable = true
    )
    public void setupServer(CallbackInfoReturnable<Boolean> info) throws IOException {
//        SandboxFabric.CORE.load();
    }
    @Inject(method = "stopServer",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;stopServer()V",
                    shift = At.Shift.BEFORE),
            cancellable = true
    )
    public void setupServer(CallbackInfo info) throws IOException {
//        SandboxFabric.CORE.unload();
    }
}
