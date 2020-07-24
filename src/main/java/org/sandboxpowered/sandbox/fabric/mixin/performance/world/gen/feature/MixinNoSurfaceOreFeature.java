package org.sandboxpowered.sandbox.fabric.mixin.performance.world.gen.feature;

import net.minecraft.util.math.Direction;
import net.minecraft.world.gen.feature.NoSurfaceOreFeature;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NoSurfaceOreFeature.class)
public class MixinNoSurfaceOreFeature {
    @Redirect(method = "checkAir", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private static Direction[] values() {
        return PerformanceUtil.DIRECTIONS;
    }
}