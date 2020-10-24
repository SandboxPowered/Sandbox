package org.sandboxpowered.sandbox.fabric.mixin.performance.direction_values.block;

import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.Direction;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LeavesBlock.class)
public class MixinLeavesBlock {
    @Redirect(method = "updateDistanceFromLogs", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private static Direction[] values() {
        return PerformanceUtil.getDirectionArray();
    }
}