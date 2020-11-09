package org.sandboxpowered.loader.fabric.mixin.content;

import net.minecraft.server.Bootstrap;
import org.sandboxpowered.loader.fabric.SandboxFabric;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Bootstrap.class)
public abstract class MixinBootstrap {
    @Shadow
    protected static void wrapStreams() {
    }

    @Redirect(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/Bootstrap;wrapStreams()V"))
    private static void wrapStreamsRedirect() {
        wrapStreams();
        SandboxFabric.CORE.init();
    }
}