package org.sandboxpowered.loader.fabric.mixin.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.loader.CacheableRegistry;
import org.sandboxpowered.loader.WrappedRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MappedRegistry.class)
public abstract class MixinMappedRegistry<T, S extends Content<S>> extends WritableRegistry<T> implements CacheableRegistry<S,T> {
    @Shadow
    private int nextId;
    private int vanillaNext;
    private boolean hasStored;
    private WrappedRegistry<S, T> wrappedRegistry;

    public MixinMappedRegistry(ResourceKey<? extends Registry<T>> resourceKey, Lifecycle lifecycle) {
        super(resourceKey, lifecycle);
    }

    @Override
    public void saveRegistryContent() {
        if (!hasStored) {
            vanillaNext = nextId;

            hasStored = true;
        }
    }

    @Override
    public void resetRegistryContent() {
        if (hasStored) {
            nextId = vanillaNext;

            hasStored = false;
        }
    }
}