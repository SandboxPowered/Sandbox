package org.sandboxpowered.sandbox.fabric.mixin.performance.direction_values.block.piston;

import net.minecraft.block.piston.PistonHandler;
import net.minecraft.util.math.Direction;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PistonHandler.class)
public class MixinPistonHandler {
    @Redirect(method = "canMoveAdjacentBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values() {
        return PerformanceUtil.getDirectionArray();
    }
}
