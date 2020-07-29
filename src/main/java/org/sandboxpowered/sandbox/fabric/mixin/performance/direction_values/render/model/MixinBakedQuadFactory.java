package org.sandboxpowered.sandbox.fabric.mixin.performance.direction_values.render.model;

import net.minecraft.client.render.model.BakedQuadFactory;
import net.minecraft.util.math.Direction;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BakedQuadFactory.class)
public class MixinBakedQuadFactory {
    @Redirect(method = "decodeDirection", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private static Direction[] values2() {
        return PerformanceUtil.DIRECTIONS;
    }

    @Redirect(method = "getPositionMatrix", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values() {
        return PerformanceUtil.DIRECTIONS;
    }

    @Redirect(method = "encodeDirection", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values3() {
        return PerformanceUtil.DIRECTIONS;
    }
}