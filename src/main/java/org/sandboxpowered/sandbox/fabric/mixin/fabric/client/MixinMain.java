package org.sandboxpowered.sandbox.fabric.mixin.fabric.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.main.Main;
import org.sandboxpowered.sandbox.api.client.Client;
import org.sandboxpowered.sandbox.fabric.SandboxCommon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Main.class)
public abstract class MixinMain {

    @Redirect(method = "main", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;start()V"))
    private static void main(MinecraftClient client) {
        SandboxCommon.client = (Client) client;
        client.start();
    }
}