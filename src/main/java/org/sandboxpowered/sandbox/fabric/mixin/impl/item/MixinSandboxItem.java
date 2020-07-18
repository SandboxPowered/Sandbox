package org.sandboxpowered.sandbox.fabric.mixin.impl.item;

import org.sandboxpowered.api.item.BaseItem;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = BaseItem.class, remap = false)
@Unique
public abstract class MixinSandboxItem implements SandboxInternal.WrappedInjection<SandboxInternal.ItemWrapper> {
    private SandboxInternal.ItemWrapper sandboxWrappedInjection;

    @Override
    public final SandboxInternal.ItemWrapper getInjectionWrapped() {
        return sandboxWrappedInjection;
    }

    @Override
    public final void setInjectionWrapped(SandboxInternal.ItemWrapper o) {
        sandboxWrappedInjection = o;
    }
}
