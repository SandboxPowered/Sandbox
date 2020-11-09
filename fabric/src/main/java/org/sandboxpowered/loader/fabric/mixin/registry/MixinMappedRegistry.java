package org.sandboxpowered.loader.fabric.mixin.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.loader.CacheableRegistry;
import org.sandboxpowered.loader.Wrappers;
import org.sandboxpowered.loader.fabric.SandboxFabric;
import org.sandboxpowered.loader.fabric.registry.WrappedRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MappedRegistry.class)
public abstract class MixinMappedRegistry<T, S extends Content<S>> extends WritableRegistry<T> implements CacheableRegistry<S, T> {
    @Shadow
    private int nextId;
    private int vanillaNext;
    private boolean hasStored;
    private WrappedRegistry<S, T> wrappedRegistry;

    public MixinMappedRegistry(ResourceKey<? extends Registry<T>> resourceKey, Lifecycle lifecycle) {
        super(resourceKey, lifecycle);
    }

    public void setWrappedRegistry(WrappedRegistry<S, T> wrappedRegistry) {
        this.wrappedRegistry = wrappedRegistry;
    }

    @Override
    public void setWrapper(Wrappers.Wrapper<S, T> wrapper) {
        setWrappedRegistry(new WrappedRegistry<>(
                Wrappers.IDENTITY.toSandbox(key().location()),
                this,
                wrapper
        ));
        SandboxFabric.CORE.getLog().debug("Setting cached registry '{}' to Sandbox: '{}' Vanilla: '{}'", wrappedRegistry.getIdentity(), wrapper.getSandboxType(), wrapper.getVanillaType());
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