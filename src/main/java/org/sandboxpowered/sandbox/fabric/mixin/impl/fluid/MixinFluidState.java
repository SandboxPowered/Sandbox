package org.sandboxpowered.sandbox.fabric.mixin.impl.fluid;

import org.sandboxpowered.api.fluid.Fluid;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.state.FluidState;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.fluid.FluidState.class)
@Implements(@Interface(iface = FluidState.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinFluidState {
    @Shadow
    public abstract net.minecraft.fluid.Fluid getFluid();

    public Fluid sbx$getFluid() {
        return WrappingUtil.convert(getFluid());
    }

    public BlockState sbx$asBlockState() {
        return sbx$getFluid().asBlockState((FluidState) this);
    }

    public boolean sbx$isStill() {
        return sbx$getFluid().isStill((FluidState) this);
    }
}