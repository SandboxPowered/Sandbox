package org.sandboxpowered.loader;

import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.registry.Registry;

public interface CacheableRegistry<S extends Content<S>, V> {
    void setWrapper(Wrappers.Wrapper<S, V> wrapper);

    Registry<S> getSandboxRegistry();

    void saveRegistryContent();

    void resetRegistryContent();

    interface Wrapped<S, V> {
        net.minecraft.core.Registry<V> toVanilla();
    }
}
