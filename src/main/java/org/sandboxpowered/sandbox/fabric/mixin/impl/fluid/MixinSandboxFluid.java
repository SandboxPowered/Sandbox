package org.sandboxpowered.sandbox.fabric.mixin.impl.fluid;

import org.sandboxpowered.api.fluid.BaseFluid;
import org.sandboxpowered.api.fluid.Fluid;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BaseFluid.class, remap = false)
public abstract class MixinSandboxFluid implements SandboxInternal.WrappedInjection<Fluid> {
    private Fluid sandboxWrappedInjection;

    @Override
    public final Fluid getInjectionWrapped() {
        return sandboxWrappedInjection;
    }

    @Override
    public final void setInjectionWrapped(Fluid o) {
        sandboxWrappedInjection = o;
    }
}