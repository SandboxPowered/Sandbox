package com.hrznstudio.sandbox.mixin.impl.block;

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
        return fluid_1==Fluids.WATER && !blockState_1.get(Properties.WATERLOGGED);
    }

    /**
     * @author Coded
     */
    @Overwrite
    default boolean tryFillWithFluid(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1, FluidState fluidState_1) {
        if (canFillWithFluid(iWorld_1, blockPos_1, blockState_1, fluidState_1.getFluid())) {
            if (!iWorld_1.isClient()) {
                iWorld_1.setBlockState(blockPos_1, blockState_1.with(Properties.WATERLOGGED, true), 3);
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
        if (blockState_1.get(Properties.WATERLOGGED)) {
            iWorld_1.setBlockState(blockPos_1, blockState_1.with(Properties.WATERLOGGED,false), 3);
            return Fluids.WATER;
        } else {
            return Fluids.EMPTY;
        }
    }
}