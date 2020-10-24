package org.sandboxpowered.sandbox.fabric.mixin.performance.particle_culling;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import org.sandboxpowered.sandbox.fabric.internal.IFrustumWorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer implements IFrustumWorldRenderer {

    private Frustum sandboxFrustum;

    @Inject(locals = LocalCapture.CAPTURE_FAILHARD,
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BackgroundRenderer;render(Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/world/ClientWorld;IF)V")
    )
    private void captureFrustum(MatrixStack stack, float f, long l, boolean b, Camera camera, GameRenderer renderer, LightmapTextureManager lightmap, Matrix4f matrix, CallbackInfo ci, Profiler iprofiler, Vec3d vec3d, double d0, double d1, double d2, Matrix4f matrix4f, boolean flag, Frustum capturedFustrum) {
        if (capturedFustrum != null)
            this.sandboxFrustum = capturedFustrum;
    }

    @Override
    public Frustum sandboxGetFrustum() {
        return this.sandboxFrustum;
    }
}
