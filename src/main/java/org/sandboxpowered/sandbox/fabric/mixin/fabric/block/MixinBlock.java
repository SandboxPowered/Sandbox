package org.sandboxpowered.sandbox.fabric.mixin.fabric.block;

import org.sandboxpowered.sandbox.fabric.util.wrapper.BlockWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MixinBlock {

    /**
     * @author B0undarybreaker
     */
    @Inject(method = "isNaturalStone", at = @At("HEAD"), cancellable = true)
    private static void getExtraNaturalStone(Block block, CallbackInfoReturnable<Boolean> info) {
        if (block instanceof BlockWrapper) info.setReturnValue(((BlockWrapper) block).getBlock().isNaturalStone());
    }

    /**
     * @author B0undarybreaker
     */
    @Inject(method = "isNaturalDirt", at = @At("HEAD"), cancellable = true)
    private static void getExtraNaturalDirt(Block block, CallbackInfoReturnable<Boolean> info) {
        if (block instanceof BlockWrapper) info.setReturnValue(((BlockWrapper) block).getBlock().isNaturalDirt());
    }

    /**
     * @author B0undarybreaker
     */
    @Deprecated
    @Inject(method = "getFluidState", at = @At("HEAD"), cancellable = true)
    private void getWaterloggedFluidState(BlockState state, CallbackInfoReturnable<FluidState> info) {
        if (state.contains(Properties.WATERLOGGED))
            info.setReturnValue(state.get(Properties.WATERLOGGED) ? Fluids.WATER.getDefaultState() : Fluids.EMPTY.getDefaultState());
    }
}