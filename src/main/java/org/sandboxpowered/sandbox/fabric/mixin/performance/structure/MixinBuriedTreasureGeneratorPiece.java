package org.sandboxpowered.sandbox.fabric.mixin.performance.structure;

import net.minecraft.structure.BuriedTreasureGenerator;
import net.minecraft.util.math.Direction;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BuriedTreasureGenerator.Piece.class)
public class MixinBuriedTreasureGeneratorPiece {
    @Redirect(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private static Direction[] values() {
        return PerformanceUtil.DIRECTIONS;
    }
}