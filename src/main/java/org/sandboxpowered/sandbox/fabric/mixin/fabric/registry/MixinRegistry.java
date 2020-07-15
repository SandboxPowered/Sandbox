package org.sandboxpowered.sandbox.fabric.mixin.fabric.registry;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Registry.class)
public abstract class MixinRegistry<T> implements SandboxInternal.RegistryKeyObtainer<T> {
    @Shadow
    @Final
    private RegistryKey<Registry<T>> registryKey;

    @Override
    public RegistryKey<Registry<T>> sandbox_getRegistryKey() {
        return registryKey;
    }
}