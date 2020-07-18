package org.sandboxpowered.sandbox.fabric.mixin.fabric.client;

import net.minecraft.server.integrated.IntegratedServer;
import org.sandboxpowered.sandbox.fabric.loader.SandboxLoader;
import org.sandboxpowered.sandbox.fabric.mixin.fabric.server.MixinMinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

@Mixin(IntegratedServer.class)
public class MixinIntegratedServer extends MixinMinecraftServer {
    @Inject(method = "setupServer",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/integrated/IntegratedServer;loadWorld()V",
                    shift = At.Shift.BEFORE),
            cancellable = true
    )
    public void setupServer(CallbackInfoReturnable<Boolean> info) {
        try {
            new SandboxLoader().load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}