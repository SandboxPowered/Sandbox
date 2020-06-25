package org.sandboxpowered.sandbox.fabric.mixin.fabric.registry;

import com.google.common.collect.BiMap;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Int2ObjectBiMap;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.sandboxpowered.sandbox.fabric.impl.BasicRegistry;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.Log;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

@Mixin(Registry.class)
public abstract class MixinRegistry<T> implements SandboxInternal.RegistryKeyObtainer<T> {
    @Shadow @Final private RegistryKey<Registry<T>> registryKey;

    @Override
    public RegistryKey<Registry<T>> sandbox_getRegistryKey() {
        return registryKey;
    }
}