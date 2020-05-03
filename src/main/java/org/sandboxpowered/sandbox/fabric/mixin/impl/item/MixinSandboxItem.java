package org.sandboxpowered.sandbox.fabric.mixin.impl.item;

import org.sandboxpowered.api.item.BaseItem;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = BaseItem.class, remap = false)
@Unique
public abstract class MixinSandboxItem implements SandboxInternal.WrappedInjection {
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