package com.hrznstudio.sandbox.mixin.impl.fluid;

import com.hrznstudio.sandbox.api.SandboxInternal;
import com.hrznstudio.sandbox.api.fluid.Fluid;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = Fluid.class, remap = false)
public abstract class MixinSandboxFluid implements SandboxInternal.WrappedInjection {
    private Object sandboxWrappedInjection;

    @Override
    public final Object getInjectionWrapped() {
        return sandboxWrappedInjection;
    }

    @Override
    public final void setInjectionWrapped(Object o) {
        sandboxWrappedInjection = o;
    }
}