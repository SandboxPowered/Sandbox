package org.sandboxpowered.sandbox.fabric.mixin.performance.direction_values.render.block;

import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.util.math.Direction;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockModelRenderer.class)
public class MixinBlockModelRenderer {
    @Redirect(method = "renderSmooth", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values() {
        return PerformanceUtil.DIRECTIONS;
    }

    @Redirect(method = "renderFlat", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values2() {
        return PerformanceUtil.DIRECTIONS;
    }

    @Redirect(method = "getQuadDimensions", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values3() {
        return PerformanceUtil.DIRECTIONS;
    }

    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/block/BlockState;Lnet/minecraft/client/render/model/BakedModel;FFFII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values4() {
        return PerformanceUtil.DIRECTIONS;
    }

    @Mixin(BlockModelRenderer.NeighborOrientation.class)
    public static class MixinNeighborOrientation {

        @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
        private Direction[] values() {
            return PerformanceUtil.DIRECTIONS;
        }
    }
}