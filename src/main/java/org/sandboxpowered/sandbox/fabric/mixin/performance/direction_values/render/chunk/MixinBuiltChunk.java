package org.sandboxpowered.sandbox.fabric.mixin.performance.direction_values.render.chunk;

import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.Direction;
import org.sandboxpowered.sandbox.fabric.util.PerformanceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkBuilder.BuiltChunk.class)
public class MixinBuiltChunk {
    @Redirect(method = "setOrigin", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;values()[Lnet/minecraft/util/math/Direction;"))
    private Direction[] values() {
        return PerformanceUtil.getDirectionArray();
    }
}