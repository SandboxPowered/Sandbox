package org.sandboxpowered.sandbox.fabric.mixin.impl.block;

import org.sandboxpowered.api.block.BaseBlock;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = BaseBlock.class, remap = false)
@Unique
public abstract class MixinSandboxBlock implements SandboxInternal.WrappedInjection<SandboxInternal.IBlockWrapper> {
    private SandboxInternal.IBlockWrapper sandboxWrappedInjection;

    @Override
    public final SandboxInternal.IBlockWrapper getInjectionWrapped() {
        return sandboxWrappedInjection;
    }

    @Override
    public final void setInjectionWrapped(SandboxInternal.IBlockWrapper o) {
        sandboxWrappedInjection = o;
    }
}