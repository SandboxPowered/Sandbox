package com.hrznstudio.sandbox.mixin.impl.fluid;

import com.hrznstudio.sandbox.SandboxProperties;
import com.hrznstudio.sandbox.api.SandboxInternal;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BaseFluid.class)
public abstract class MixinBaseFluid extends Fluid {
    @Shadow
    protected abstract FluidState getUpdatedState(ViewableWorld viewableWorld_1, BlockPos blockPos_1, BlockState blockState_1);

    @Shadow
    protected abstract int getNextTickDelay(World world_1, BlockPos blockPos_1, FluidState fluidState_1, FluidState fluidState_2);

    @Shadow
    protected abstract void method_15725(IWorld iWorld_1, BlockPos blockPos_1, FluidState fluidState_1);

    /**
     * @author Coded
     */
    @Overwrite
    public void onScheduledTick(World world_1, BlockPos blockPos_1, FluidState fluidState_1) {
        FluidState state = null;
        if (!fluidState_1.isStill()) {
            FluidState fluidState_2 = this.getUpdatedState(world_1, blockPos_1, world_1.getBlockState(blockPos_1));
            if (fluidState_2.isEmpty()) {
                state = fluidState_2;
            } else if (!fluidState_2.equals(fluidState_1)) {
                state = fluidState_2;
            }
        }

        if (state != null) {
            BlockState s = world_1.getBlockState(blockPos_1);
            int int_1 = this.getNextTickDelay(world_1, blockPos_1, fluidState_1, state);
            if (s.isAir() || s.getBlock().getStateFactory().getProperty("fluidlogged") == null || (s.getMaterial().isBurnable() && this.matches(FluidTags.LAVA))) {
                world_1.setBlockState(blockPos_1, state.isEmpty() ? Blocks.AIR.getDefaultState() : state.getBlockState(), 3);
                if (!state.isEmpty()) {
                    world_1.getFluidTickScheduler().schedule(blockPos_1, state.getFluid(), int_1);
                    world_1.updateNeighborsAlways(blockPos_1, s.getBlock());
                }
            } else {
                world_1.setBlockState(blockPos_1, s.with(SandboxProperties.PROPERTY_FLUIDLOGGABLE, ((SandboxInternal.FluidStateCompare) state).getComparability()), 3);
                if (!state.isEmpty()) {
                    world_1.getFluidTickScheduler().schedule(blockPos_1, state.getFluid(), int_1);
                    world_1.updateNeighborsAlways(blockPos_1, s.getBlock());
                }
            }
        }

        this.method_15725(world_1, blockPos_1, fluidState_1);
    }
}