package org.sandboxpowered.sandbox.fabric.mixin.fabric.block;

import net.minecraft.block.Block;
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

@Mixin(Block.class)
public class MixinBlock {

    /**
     * @author B0undarybreaker
     */
    @Deprecated
    @Inject(method = "getFluidState", at = @At("HEAD"), cancellable = true)
    private void getWaterloggedFluidState(BlockState state, CallbackInfoReturnable<FluidState> info) {
        if (state.contains(Properties.WATERLOGGED))
            info.setReturnValue(state.get(Properties.WATERLOGGED) ? Fluids.WATER.getDefaultState() : Fluids.EMPTY.getDefaultState());
    }

    @Mixin(Block.Settings.class)
    public static abstract class MixinSettings implements SandboxInternal.MaterialInternal {
        @Shadow
        protected abstract Block.Settings lightLevel(int i);

        @Override
        public void sbxsetlevel(int level) {
            lightLevel(level);
        }
    }
}