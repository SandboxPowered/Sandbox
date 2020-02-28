package org.sandboxpowered.sandbox.fabric.mixin.fabric;

import net.minecraft.Bootstrap;
import org.sandboxpowered.sandbox.fabric.SandboxHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bootstrap.class)
public class MixinBootstrapClient {
    @Shadow
    private static boolean initialized;

    @Inject(method = "initialize", at = @At("HEAD"))
    private static void init(CallbackInfo info) {
        if (!initialized) {
            SandboxHooks.setupGlobal();
        }
    }
}
