package com.hrznstudio.sandbox.mixin.fabric.fluid;

import com.hrznstudio.sandbox.api.state.FluidProperty;
import com.hrznstudio.sandbox.api.state.FluidState;
import com.hrznstudio.sandbox.util.wrapper.FluidComparability;
import org.spongepowered.asm.mixin.*;

@Mixin(FluidComparability.class)
@Implements(@Interface(iface = FluidProperty.class, prefix = "sbx$"))
@Unique
public abstract class MixinFluidComparability implements Comparable<FluidProperty> {
    @Shadow
    public abstract int compareTo(FluidComparability o);

    @Shadow
    public abstract net.minecraft.fluid.FluidState getFluidState();

    public FluidState sbx$getFluidState() {
        return (FluidState) getFluidState();
    }

    @Override
    public int compareTo(FluidProperty o) {
        return this.compareTo((FluidComparability) (Object) o);
    }
}