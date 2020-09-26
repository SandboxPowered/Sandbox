package org.sandboxpowered.sandbox.fabric.mixin.impl.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "getBasicProjectionMatrix", at = @At("HEAD"), cancellable = true)
    public void getBasicProjectionMatrix(Camera camera, float f, boolean bl, CallbackInfoReturnable<Matrix4f> info) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.peek().getModel().loadIdentity();
        SandboxInternal.MagicMatrix matrix = WrappingUtil.cast(matrixStack.peek().getModel(), SandboxInternal.MagicMatrix.class);
        matrix.sandbox_ortho(-1000f, 1000f, client.getWindow().getWidth(), client.getWindow().getHeight());
        info.setReturnValue(matrixStack.peek().getModel());
    }
}