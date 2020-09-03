package org.sandboxpowered.sandbox.fabric.mixin.editor;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import org.sandboxpowered.api.editor.Area;
import org.sandboxpowered.api.util.math.Color;
import org.sandboxpowered.api.util.math.Matrix3f;
import org.sandboxpowered.api.util.math.Vec3f;
import org.sandboxpowered.sandbox.fabric.editor.injection.EditorWorld;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    @Shadow
    private ClientWorld world;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;checkEmpty(Lnet/minecraft/client/util/math/MatrixStack;)V", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
    public void render(MatrixStack matrixStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo ci, Profiler profiler, Vec3d vec3d, double d, double e, double g, Matrix4f matrix4f2, boolean bl2, Frustum frustum2, boolean bl4, VertexConsumerProvider.Immediate immediate) {
        matrixStack.push();
        EditorWorld world = (EditorWorld) this.world;

        matrixStack.translate(-d, -e, -g);

        RenderLayer layer = RenderLayer.getLines();
        VertexConsumer consumer = immediate.getBuffer(layer);

        for (Area area : world.editor_getAreas()) {
            drawArea(matrixStack, consumer, area);
        }

        matrixStack.pop();
    }

    private static void drawArea(MatrixStack stack, VertexConsumer consumer, Area area) {
        WorldRenderer.drawBox(stack, consumer, WrappingUtil.convert(area.getBox()), 1.0F, 0.25F, 0.25F, 1.0F);
    }

    private static void drawLine(VertexConsumer consumer, MatrixStack stack, Vec3f from, Vec3f to, float r, float g, float b) {
        Matrix4f matrices = stack.peek().getModel();
        consumer.vertex(matrices, from.getX(), from.getY(), from.getZ()).color(r, g, b, 1f).next();
        consumer.vertex(matrices, to.getX(), to.getY(), to.getZ()).color(r, g, b, 1f).next();
    }
}