package org.sandboxpowered.sandbox.fabric.mixin.fabric.client;

import net.minecraft.client.options.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameOptions.class)
public class MixinGameOptions {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/options/GameOptions;load()V"))
    public void defaultOptions(GameOptions options) {
        options.autoJump = false;

        options.load();
    }
}
