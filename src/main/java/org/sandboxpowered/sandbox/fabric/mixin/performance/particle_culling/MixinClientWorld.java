package org.sandboxpowered.sandbox.fabric.mixin.performance.particle_culling;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Box;
import org.sandboxpowered.sandbox.fabric.SandboxConfig;
import org.sandboxpowered.sandbox.fabric.internal.IFrustumWorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class MixinClientWorld {

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", at = @At(value = "HEAD"), cancellable = true)
    private void addParticle(ParticleEffect particleEffect, double d, double e, double f, double g, double h, double i, CallbackInfo info) {
        cullUnimportantParticles(particleEffect, false, d, e, f, info);
    }

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;ZDDDDDD)V", at = @At(value = "HEAD"), cancellable = true)
    private void addParticle(ParticleEffect particleEffect, boolean bl, double d, double e, double f, double g, double h, double i, CallbackInfo info) {
        cullUnimportantParticles(particleEffect, bl, d, e, f, info);
    }

    private void cullUnimportantParticles(ParticleEffect particleEffect, boolean bl, double d, double e, double f, CallbackInfo info) {
        if (SandboxConfig.cullParticles.getBoolean()) {
            boolean shouldSpawn = particleEffect.getType().shouldAlwaysSpawn() || bl;
            if (!shouldSpawn && !((IFrustumWorldRenderer) MinecraftClient.getInstance().worldRenderer).sandboxGetFrustum().isVisible(new Box(d, e, f, d + 0.25f, e + 0.25f, f + 0.25f))) {
                info.cancel();
            }
        }
    }
}