package org.sandboxpowered.sandbox.fabric.mixin.performance.particle_culling;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import org.sandboxpowered.sandbox.fabric.SandboxConfig;
import org.sandboxpowered.sandbox.fabric.internal.IFrustumWorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ParticleManager.class)
public class MixinParticleManager {
    @Redirect(method = "renderParticles",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/particle/Particle;buildGeometry(Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/render/Camera;F)V"))
    private void cullParticles(Particle particle, VertexConsumer consumer, Camera camera, float partialTicks) {
        if (SandboxConfig.cullParticles.get()) {
            if (((IFrustumWorldRenderer) MinecraftClient.getInstance().worldRenderer).sandboxGetFrustum().isVisible(particle.getBoundingBox())) {
                particle.buildGeometry(consumer, camera, partialTicks);
            }
        } else {
            particle.buildGeometry(consumer, camera, partialTicks);
        }
    }

    @Inject(locals = LocalCapture.CAPTURE_FAILHARD,
            method = "addBlockBreakingParticles",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/shape/VoxelShape;getBoundingBox()Lnet/minecraft/util/math/Box;", shift = At.Shift.BY, by = 2),
            cancellable = true
    )
    private void addBlockBreakingParticles(BlockPos blockPos, Direction direction, CallbackInfo ci, BlockState state, int i, int j, int k, float f, Box box) {
        if (SandboxConfig.cullParticles.get()) {
            Frustum frustum = ((IFrustumWorldRenderer) MinecraftClient.getInstance().worldRenderer).sandboxGetFrustum();
            if (frustum != null && frustum.isVisible(box.offset(blockPos))) {
                ci.cancel();
            }
        }
    }

    @Inject(locals = LocalCapture.CAPTURE_FAILHARD,
            method = "addBlockBreakParticles",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/shape/VoxelShape;", shift = At.Shift.BY, by = 2),
            cancellable = true
    )
    private void addBlockBreakParticles(BlockPos blockPos, BlockState state, CallbackInfo ci, VoxelShape shape) {
        if (SandboxConfig.cullParticles.get()) {
            Frustum frustum = ((IFrustumWorldRenderer) MinecraftClient.getInstance().worldRenderer).sandboxGetFrustum();
            if (frustum != null && shape.getBoundingBoxes().stream().map(box -> box.offset(blockPos)).noneMatch(frustum::isVisible)) {
                ci.cancel();
            }
        }
    }

}