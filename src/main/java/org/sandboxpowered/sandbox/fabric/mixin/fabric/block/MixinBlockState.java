package org.sandboxpowered.sandbox.fabric.mixin.fabric.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class MixinBlockState {

    @Shadow
    public abstract FluidState getFluidState();

    @SuppressWarnings("ConstantConditions")
    @Inject(at = @At("RETURN"), method = "getLuminance", cancellable = true)
    public void onGetLuminance(CallbackInfoReturnable<Integer> info) {
        FluidState fluidState = getFluidState();
        if (fluidState.getFluid() != Fluids.EMPTY) {
            BlockState fluidBlockState = fluidState.getBlockState();
            if (fluidBlockState != (Object) this) {
                info.setReturnValue(Math.max(info.getReturnValue(), fluidBlockState.getLuminance()));
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getStateForNeighborUpdate")
    public void onGetStateForNeighborUpdate(Direction direction_1, BlockState blockState_1, WorldAccess iWorld_1, BlockPos blockPos_1, BlockPos blockPos_2, CallbackInfoReturnable<BlockState> info) {
        FluidState state = getFluidState();
        if (!state.isEmpty()) {
            iWorld_1.getFluidTickScheduler().schedule(blockPos_1, state.getFluid(), state.getFluid().getTickRate(iWorld_1));
        }
    }
}