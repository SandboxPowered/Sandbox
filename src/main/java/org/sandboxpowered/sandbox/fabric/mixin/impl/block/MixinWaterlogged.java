package org.sandboxpowered.sandbox.fabric.mixin.impl.block;

import net.minecraft.block.Waterloggable;
import org.sandboxpowered.sandbox.api.block.FluidLoggable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Waterloggable.class)
public interface MixinWaterlogged extends FluidLoggable {
    @Override
    default boolean needsWaterloggedProperty() {
        return false;
    }
}