package org.sandboxpowered.sandbox.fabric.mixin.performance.entity.ai.pathing;

import net.minecraft.entity.ai.pathing.AmphibiousPathNodeMaker;
import net.minecraft.util.math.Direction;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AmphibiousPathNodeMaker.class)
public class MixinAmphibiousPathNodeMarker {
    @Redirect(method = "getDefaultNodeType", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private static Direction[] values() {
        return PerformanceUtil.DIRECTIONS;
    }
}