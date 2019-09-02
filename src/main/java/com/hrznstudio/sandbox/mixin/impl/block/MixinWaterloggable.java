package com.hrznstudio.sandbox.mixin.impl.block;

import com.hrznstudio.sandbox.Sandbox;
import com.hrznstudio.sandbox.SandboxProperties;
import com.hrznstudio.sandbox.api.SandboxInternal;
import com.hrznstudio.sandbox.util.wrapper.FluidComparability;
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
        return SandboxProperties.PROPERTY_FLUIDLOGGABLE.isValid(fluid_1) && blockState_1.get(SandboxProperties.PROPERTY_FLUIDLOGGABLE).getFluidState().isEmpty();
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
        FluidState comparability = blockState_1.get(SandboxProperties.PROPERTY_FLUIDLOGGABLE).getFluidState();
        if (!comparability.isEmpty()) {
            iWorld_1.setBlockState(blockPos_1, blockState_1.with(SandboxProperties.PROPERTY_FLUIDLOGGABLE, ((SandboxInternal.FluidStateCompare) Fluids.EMPTY.getDefaultState()).getComparability()), 3);
            return comparability.getFluid();
        } else {
            return Fluids.EMPTY;
        }
    }
}