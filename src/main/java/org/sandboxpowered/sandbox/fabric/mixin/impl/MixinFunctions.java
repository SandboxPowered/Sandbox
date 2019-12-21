package org.sandboxpowered.sandbox.fabric.mixin.impl;

import org.sandboxpowered.sandbox.api.util.Functions;
import org.sandboxpowered.sandbox.fabric.impl.FunctionsImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nonnull;

@Mixin(Functions.class)
public interface MixinFunctions {
    /**
     * @author Coded
     */
    @Overwrite(remap = false)
    @Nonnull
    static Functions getInstance() {
        return FunctionsImpl.INSTANCE;
    }
}