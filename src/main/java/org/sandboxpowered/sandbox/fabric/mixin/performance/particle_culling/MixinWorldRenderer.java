package org.sandboxpowered.sandbox.fabric.mixin.performance.particle_culling;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.sandbox.fabric.internal.IFrustumWorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer implements IFrustumWorldRenderer {

    private Frustum sandbox_frustum;

    @Inject(locals = LocalCapture.CAPTURE_FAILHARD,
            method = "render",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/client/render/BackgroundRenderer;render(Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/world/ClientWorld;IF)V")
    )
    private void captureFrustum(MatrixStack p_228426_1_, float p_228426_2_, long p_228426_3_, boolean p_228426_5_, Camera p_228426_6_, GameRenderer p_228426_7_, LightmapTextureManager p_228426_8_, Matrix4f p_228426_9_, CallbackInfo ci, Profiler iprofiler, Vec3d vec3d, double d0, double d1, double d2, Matrix4f matrix4f, boolean flag, Frustum capturedFustrum) {
        this.sandbox_frustum = capturedFustrum;
    }

    @Override
    public Frustum sandbox_getFrustum() {
        return this.sandbox_frustum;
    }
}
