package org.sandboxpowered.loader.inject;

import com.google.inject.Inject;
import org.sandboxpowered.api.inject.FactoryProvider;
import org.sandboxpowered.api.inject.Implementation;

public class SandboxImplementation implements Implementation {
    private final FactoryProvider provider;

    @Inject
    public SandboxImplementation(FactoryProvider provider) {
        this.provider = provider;
    }

    @Override
    public FactoryProvider getFactoryProvider() {
        return provider;
    }
}
