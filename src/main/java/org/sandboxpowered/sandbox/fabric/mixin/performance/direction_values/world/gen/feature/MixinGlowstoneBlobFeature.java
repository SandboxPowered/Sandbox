package org.sandboxpowered.sandbox.fabric.mixin.performance.direction_values.world.gen.feature;

import net.minecraft.util.math.Direction;
import net.minecraft.world.gen.feature.GlowstoneBlobFeature;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GlowstoneBlobFeature.class)
public class MixinGlowstoneBlobFeature {
    @Redirect(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values() {
        return PerformanceUtil.getDirectionArray();
    }
}