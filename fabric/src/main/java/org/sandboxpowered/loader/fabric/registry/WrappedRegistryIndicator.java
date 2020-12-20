package org.sandboxpowered.loader.fabric.registry;

import org.sandboxpowered.api.content.Content;

public interface WrappedRegistryIndicator<S extends Content<S>,V> {
    WrappedRegistry<S,V> sandbox_getWrappedRegistry();
}
