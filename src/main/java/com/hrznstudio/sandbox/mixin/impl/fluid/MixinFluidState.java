package com.hrznstudio.sandbox.mixin.impl.fluid;

import com.hrznstudio.sandbox.api.SandboxInternal;
import com.hrznstudio.sandbox.api.fluid.IFluid;
import com.hrznstudio.sandbox.api.state.FluidState;
import com.hrznstudio.sandbox.util.WrappingUtil;
import com.hrznstudio.sandbox.util.wrapper.FluidComparability;
import net.minecraft.fluid.Fluid;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.fluid.FluidStateImpl.class)
@Implements(@Interface(iface = FluidState.class, prefix = "sbx$"))
@Unique
public abstract class MixinFluidState implements SandboxInternal.FluidStateCompare {
    private FluidComparability comparability;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void constructor(CallbackInfo info) {
        comparability = new FluidComparability((net.minecraft.fluid.FluidState) (Object) this);
    }

    @Override
    public FluidComparability getComparability() {
        return comparability;
    }
    @Shadow
    public abstract Fluid getFluid();

    public IFluid sbx$getFluid() {
        return WrappingUtil.convert(getFluid());
    }
}