package org.sandboxpowered.sandbox.fabric.mixin.performance.particle_culling;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import org.sandboxpowered.sandbox.fabric.SandboxConfig;
import org.sandboxpowered.sandbox.fabric.internal.IFrustumWorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ParticleManager.class)
public class MixinParticleManager {
    @Redirect(method = "renderParticles",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/particle/Particle;buildGeometry(Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/render/Camera;F)V"))
    private void cullParticles(Particle particle, VertexConsumer consumer, Camera camera, float partialTicks) {
        if (SandboxConfig.cullParticles.get()) {
            if (((IFrustumWorldRenderer) MinecraftClient.getInstance().worldRenderer).sandbox_getFrustum().isVisible(particle.getBoundingBox())) {
                particle.buildGeometry(consumer, camera, partialTicks);
            }
        } else {
            particle.buildGeometry(consumer, camera, partialTicks);
        }
    }
}