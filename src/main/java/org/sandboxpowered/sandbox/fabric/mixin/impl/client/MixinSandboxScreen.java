package org.sandboxpowered.sandbox.fabric.mixin.impl.client;

import org.sandboxpowered.sandbox.api.SandboxInternal;
import org.sandboxpowered.sandbox.api.client.screen.BaseScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = BaseScreen.class, remap = false)
@Unique
public abstract class MixinSandboxScreen implements SandboxInternal.WrappedInjection {
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