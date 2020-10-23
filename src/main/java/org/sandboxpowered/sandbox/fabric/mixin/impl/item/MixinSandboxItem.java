package org.sandboxpowered.sandbox.fabric.mixin.impl.item;

import org.sandboxpowered.api.item.BaseItem;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = BaseItem.class, remap = false)
@Unique
public abstract class MixinSandboxItem implements SandboxInternal.WrappedInjection<SandboxInternal.IItemWrapper> {
    private SandboxInternal.IItemWrapper sandboxWrappedInjection;

    @Override
    public final SandboxInternal.IItemWrapper getInjectionWrapped() {
        return sandboxWrappedInjection;
    }

    @Override
    public final void setInjectionWrapped(SandboxInternal.IItemWrapper o) {
        sandboxWrappedInjection = o;
    }
}
