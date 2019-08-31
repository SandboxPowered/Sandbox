package com.hrznstudio.sandbox.mixin.impl.client;

import com.hrznstudio.sandbox.api.SandboxInternal;
import com.hrznstudio.sandbox.api.client.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Screen.class)
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