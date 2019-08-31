package com.hrznstudio.sandbox.mixin.impl.enchant;

import com.hrznstudio.sandbox.api.SandboxInternal;
import com.hrznstudio.sandbox.api.enchant.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Enchantment.class)
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