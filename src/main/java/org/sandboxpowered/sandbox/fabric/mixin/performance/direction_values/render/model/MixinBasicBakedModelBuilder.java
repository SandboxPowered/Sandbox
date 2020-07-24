package org.sandboxpowered.sandbox.fabric.mixin.performance.direction_values.render.model;

import net.minecraft.client.render.model.BasicBakedModel;
import net.minecraft.util.math.Direction;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BasicBakedModel.Builder.class)
public class MixinBasicBakedModelBuilder {
    @Redirect(method = "<init>(ZZZLnet/minecraft/client/render/model/json/ModelTransformation;Lnet/minecraft/client/render/model/json/ModelOverrideList;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values() {
        return PerformanceUtil.DIRECTIONS;
    }
}