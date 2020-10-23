package org.sandboxpowered.sandbox.fabric.mixin.performance.direction_values.block.entity;

import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.util.math.Direction;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndGatewayBlockEntity.class)
public class MixinEndGatewayBlockEntity {
    @Redirect(method = "getDrawnSidesCount", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values() {
        return PerformanceUtil.getDirectionArray();
    }
}