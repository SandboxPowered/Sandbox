package org.sandboxpowered.loader;

public interface CacheableRegistry<S,V> {
    void saveRegistryContent();

    void resetRegistryContent();
}
