package org.sandboxpowered.sandbox.fabric.mixin.performance.direction_values.entity.ai.pathing;

import net.minecraft.entity.ai.pathing.WaterPathNodeMaker;
import net.minecraft.util.math.Direction;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WaterPathNodeMaker.class)
public class MixinWaterPathNodeMarker {
    @Redirect(method = "getSuccessors", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values() {
        return PerformanceUtil.DIRECTIONS;
    }
}