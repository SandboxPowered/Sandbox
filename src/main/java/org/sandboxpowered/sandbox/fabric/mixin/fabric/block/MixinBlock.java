package org.sandboxpowered.sandbox.fabric.mixin.fabric.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.ToIntFunction;

@Mixin(AbstractBlock.class)
public class MixinBlock {

    /**
     * @author B0undarybreaker
     */
    @Inject(method = "getFluidState", at = @At("HEAD"), cancellable = true)
    private void getWaterloggedFluidState(BlockState state, CallbackInfoReturnable<FluidState> info) {
        if (state.contains(Properties.WATERLOGGED))
            info.setReturnValue(state.get(Properties.WATERLOGGED) ? Fluids.WATER.getDefaultState() : Fluids.EMPTY.getDefaultState());
    }

    @Mixin(Settings.class)
    public abstract static class MixinSettings implements SandboxInternal.MaterialInternal {

        @Shadow public abstract Settings luminance(ToIntFunction<BlockState> toIntFunction);

        @Override
        public void setLevel(int level) {
            luminance(state -> level);
        }
    }
}
