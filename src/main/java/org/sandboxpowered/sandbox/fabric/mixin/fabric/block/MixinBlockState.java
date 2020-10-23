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
@SuppressWarnings({"java:S100","java:S1610"})
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
    public void onGetStateForNeighborUpdate(Direction direction, BlockState state, WorldAccess world, BlockPos pos, BlockPos otherPos, CallbackInfoReturnable<BlockState> info) {
        FluidState fluidState = getFluidState();
        if (!fluidState.isEmpty()) {
            world.getFluidTickScheduler().schedule(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
        }
    }
}