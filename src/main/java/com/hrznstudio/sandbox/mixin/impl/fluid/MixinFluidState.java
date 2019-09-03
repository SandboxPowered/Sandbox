package com.hrznstudio.sandbox.mixin.impl.fluid;

import com.hrznstudio.sandbox.api.fluid.IFluid;
import com.hrznstudio.sandbox.api.state.BlockState;
import com.hrznstudio.sandbox.api.state.FluidState;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.fluid.Fluid;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.fluid.FluidStateImpl.class)
@Implements(@Interface(iface = FluidState.class, prefix = "sbx$"))
@Unique
public abstract class MixinFluidState {
    @Shadow
    public abstract Fluid getFluid();

    public IFluid sbx$getFluid() {
        return WrappingUtil.convert(getFluid());
    }

    public BlockState sbx$asBlockState() {
        return sbx$getFluid().asBlockState((FluidState)this);
    }

    public boolean sbx$isStill() {
        return sbx$getFluid().isStill((FluidState)this);
    }
}