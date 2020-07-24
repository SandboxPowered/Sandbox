package org.sandboxpowered.sandbox.fabric.mixin.performance.block;

import net.minecraft.block.FireBlock;
import net.minecraft.util.math.Direction;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireBlock.class)
public class MixinFireBlock {
    @Redirect(method = "getStateForPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private static Direction[] values() {
        return PerformanceUtil.DIRECTIONS;
    }

    @Redirect(method = "areBlocksAroundFlammable", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private static Direction[] values2() {
        return PerformanceUtil.DIRECTIONS;
    }

    @Redirect(method = "getBurnChance(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private static Direction[] values3() {
        return PerformanceUtil.DIRECTIONS;
    }
}