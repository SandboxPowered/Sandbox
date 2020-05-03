package org.sandboxpowered.sandbox.fabric.mixin.impl.enchant;

import org.sandboxpowered.api.enchantment.BaseEnchantment;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = BaseEnchantment.class, remap = false)
@Unique
public abstract class MixinSandboxEnchantment implements SandboxInternal.WrappedInjection {
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