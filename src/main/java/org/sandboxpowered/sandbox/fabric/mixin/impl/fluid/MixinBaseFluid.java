package org.sandboxpowered.sandbox.fabric.mixin.impl.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BaseFluid.class)
public abstract class MixinBaseFluid extends Fluid implements SandboxInternal.BaseFluid {
    @Shadow
    protected abstract FluidState getUpdatedState(ViewableWorld viewableWorld_1, BlockPos blockPos_1, BlockState blockState_1);

    @Shadow
    protected abstract int getNextTickDelay(World world_1, BlockPos blockPos_1, FluidState fluidState_1, FluidState fluidState_2);

    @Shadow
    protected abstract void method_15725(IWorld iWorld_1, BlockPos blockPos_1, FluidState fluidState_1);

    @Shadow
    protected abstract boolean isInfinite();

    @Override
    public boolean sandboxinfinite() {
        return isInfinite();
    }
}