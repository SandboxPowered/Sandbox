package org.sandboxpowered.sandbox.fabric.mixin.performance.direction_values.block;

import net.minecraft.block.CoralBlockBlock;
import net.minecraft.util.math.Direction;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CoralBlockBlock.class)
public class MixinCoralBlockBlock {
    @Redirect(method = "isInWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values() {
        return PerformanceUtil.DIRECTIONS;
    }
}