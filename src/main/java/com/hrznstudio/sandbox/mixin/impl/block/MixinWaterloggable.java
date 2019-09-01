package com.hrznstudio.sandbox.mixin.impl.block;

import com.hrznstudio.sandbox.Sandbox;
import com.hrznstudio.sandbox.SandboxProperties;
import com.hrznstudio.sandbox.api.SandboxInternal;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Waterloggable.class)
public interface MixinWaterloggable {
    /**
     * @author Coded
     */
    @Overwrite
    default boolean canFillWithFluid(BlockView blockView_1, BlockPos blockPos_1, BlockState blockState_1, Fluid fluid_1) {
        FluidState state = blockState_1.get(SandboxProperties.PROPERTY_FLUIDLOGGABLE).getFluidState();
        return SandboxProperties.PROPERTY_FLUIDLOGGABLE.isValid(fluid_1) && (state.isEmpty() || state.getFluid().matchesType(fluid_1)) && (fluid_1 != Fluids.LAVA || !blockState_1.getMaterial().isBurnable());
    }

    /**
     * @author Coded
     */
    @Overwrite
    default boolean tryFillWithFluid(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1, FluidState fluidState_1) {
        if (canFillWithFluid(iWorld_1, blockPos_1, blockState_1, fluidState_1.getFluid())) {
            if (!iWorld_1.isClient()) {
                iWorld_1.setBlockState(blockPos_1, blockState_1.with(SandboxProperties.PROPERTY_FLUIDLOGGABLE, ((SandboxInternal.FluidStateCompare) fluidState_1).getComparability()), 3);
                iWorld_1.getFluidTickScheduler().schedule(blockPos_1, fluidState_1.getFluid(), fluidState_1.getFluid().getTickRate(iWorld_1));
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author Coded
     */
    @Overwrite
    default Fluid tryDrainFluid(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1) {
        return Fluids.EMPTY;
    }
}