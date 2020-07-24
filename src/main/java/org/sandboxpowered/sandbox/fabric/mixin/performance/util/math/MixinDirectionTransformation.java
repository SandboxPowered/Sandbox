package org.sandboxpowered.sandbox.fabric.mixin.performance.util.math;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.DirectionTransformation;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DirectionTransformation.class)
public class MixinDirectionTransformation {
    @Redirect(method = "map", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private static Direction[] values() {
        return PerformanceUtil.DIRECTIONS;
    }
}