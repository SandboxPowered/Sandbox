package org.sandboxpowered.sandbox.fabric.mixin.event.client;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import org.sandboxpowered.api.client.rendering.events.RenderEvents;
import org.sandboxpowered.api.world.World;
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
        matrixStack.translate(-d, -e, -g);

        if (RenderEvents.RENDER_IN_WORLD.hasSubscribers()) {
            World sbxWorld = WrappingUtil.convert(this.world);
            org.sandboxpowered.api.util.math.MatrixStack sbxStack = WrappingUtil.convert(matrixStack);
            org.sandboxpowered.api.client.rendering.VertexConsumer.Provider sbxProvider = WrappingUtil.convert(immediate);
            RenderEvents.RENDER_IN_WORLD.post(event -> event.onEvent(sbxWorld, sbxStack, sbxProvider));
        }

        matrixStack.pop();
    }
}