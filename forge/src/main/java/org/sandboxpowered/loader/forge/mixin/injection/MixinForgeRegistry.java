package org.sandboxpowered.loader.forge.mixin.injection;

import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.registry.Registry;
import org.sandboxpowered.loader.CacheableRegistry;
import org.sandboxpowered.loader.Wrappers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ForgeRegistry.class)
public class MixinForgeRegistry<S extends Content<S>, V extends IForgeRegistryEntry<V>> implements CacheableRegistry<S,V> {
    private Wrappers.Wrapper<S,V> sandbox_wrapper;

    /**
     * @author Coded
     */
    @Overwrite(remap = false)
    public boolean isLocked() {
        return false;
    }

    @Override
    public void setWrapper(Wrappers.Wrapper<S, V> wrapper) {
        sandbox_wrapper = wrapper;
    }

    @Override
    public Registry<S> getSandboxRegistry() {
        return null;
    }

    @Override
    public void saveRegistryContent() {

    }

    @Override
    public void resetRegistryContent() {

    }
}
