package org.sandboxpowered.sandbox.fabric.mixin.performance.world.chunk;

import net.minecraft.util.math.Direction;
import net.minecraft.world.chunk.UpgradeData;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(UpgradeData.class)
public class MixinUpgradeData {
    @Redirect(method = "upgradeSide", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private static Direction[] values() {
        return PerformanceUtil.DIRECTIONS;
    }

    @Redirect(method = "upgradeCenter", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private static Direction[] values2() {
        return PerformanceUtil.DIRECTIONS;
    }
}