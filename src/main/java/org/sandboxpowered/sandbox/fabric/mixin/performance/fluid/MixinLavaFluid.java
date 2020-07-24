package org.sandboxpowered.sandbox.fabric.mixin.performance.fluid;

import net.minecraft.fluid.LavaFluid;
import net.minecraft.util.math.Direction;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LavaFluid.class)
public class MixinLavaFluid {
    @Redirect(method = "canLightFire", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private static Direction[] values() {
        return PerformanceUtil.DIRECTIONS;
    }
}