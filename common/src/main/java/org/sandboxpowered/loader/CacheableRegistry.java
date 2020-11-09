package org.sandboxpowered.loader;

import org.sandboxpowered.api.content.Content;

public interface CacheableRegistry<S extends Content<S>, V> {
    void setWrapper(Wrappers.Wrapper<S, V> wrapper);

    void saveRegistryContent();

    void resetRegistryContent();
}
