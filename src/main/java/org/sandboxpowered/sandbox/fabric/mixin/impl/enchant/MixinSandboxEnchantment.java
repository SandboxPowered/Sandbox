package org.sandboxpowered.sandbox.fabric.mixin.impl.enchant;

import org.sandboxpowered.api.enchantment.BaseEnchantment;
import org.sandboxpowered.api.enchantment.Enchantment;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.wrapper.EnchantmentWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = BaseEnchantment.class, remap = false)
@Unique
public abstract class MixinSandboxEnchantment implements SandboxInternal.WrappedInjection<EnchantmentWrapper> {
    private EnchantmentWrapper sandboxWrappedInjection;

    @Override
    public final EnchantmentWrapper getInjectionWrapped() {
        return sandboxWrappedInjection;
    }

    @Override
    public final void setInjectionWrapped(EnchantmentWrapper o) {
        sandboxWrappedInjection = o;
    }
}
