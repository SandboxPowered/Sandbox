package org.sandboxpowered.sandbox.fabric.mixin.performance.state.property;

import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.Direction;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DirectionProperty.class)
public class MixinDirectionProperty {
    @Redirect(method = "of(Ljava/lang/String;Ljava/util/function/Predicate;)Lnet/minecraft/state/property/DirectionProperty;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private static Direction[] values() {
        return PerformanceUtil.DIRECTIONS;
    }
}