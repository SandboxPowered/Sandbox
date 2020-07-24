package org.sandboxpowered.sandbox.fabric.mixin.performance.direction_values.block;

import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.Direction;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RedstoneWireBlock.class)
public class MixinRedstoneWireBlock {
    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values() {
        return PerformanceUtil.DIRECTIONS;
    }

    @Redirect(method = "updateNeighbors", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values2() {
        return PerformanceUtil.DIRECTIONS;
    }

    @Redirect(method = "onStateReplaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values3() {
        return PerformanceUtil.DIRECTIONS;
    }
}