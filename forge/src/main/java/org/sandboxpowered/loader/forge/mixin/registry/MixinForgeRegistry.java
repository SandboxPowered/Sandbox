package org.sandboxpowered.loader.forge.mixin.registry;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.loader.CacheableRegistry;
import org.sandboxpowered.loader.Wrappers;
import org.sandboxpowered.loader.forge.SandboxForgeCore;
import org.sandboxpowered.loader.forge.registry.WrappedRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ForgeRegistry.class)
public abstract class MixinForgeRegistry<V extends IForgeRegistryEntry<V>, S extends Content<S>> implements CacheableRegistry<S, V>, IForgeRegistry<V> {
    @Shadow
    @Final
    private RegistryKey<Registry<V>> key;
    private WrappedRegistry<S, V> wrappedRegistry;

    public void setWrappedRegistry(WrappedRegistry<S, V> wrappedRegistry) {
        this.wrappedRegistry = wrappedRegistry;
    }

    @Override
    public void setWrapper(Wrappers.Wrapper<S, V> wrapper) {
        setWrappedRegistry(new WrappedRegistry<>(
                Wrappers.IDENTITY.toSandbox(key.location()),
                this,
                wrapper
        ));
        SandboxForgeCore.CORE.getLog().info("Setting cached registry '{}' to Sandbox: '{}' Vanilla: '{}'", wrappedRegistry.getIdentity(), wrapper.getSandboxType(), wrapper.getVanillaType());
    }

    @Override
    public void saveRegistryContent() {

    }

    @Override
    public void resetRegistryContent() {

    }
}
