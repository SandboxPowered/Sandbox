package org.sandboxpowered.loader.fabric.inject;

import org.sandboxpowered.loader.fabric.SandboxFabric;
import org.sandboxpowered.loader.inject.SandboxImplementationModule;
import org.sandboxpowered.loader.platform.SandboxPlatform;

public class FabricImplementationModule extends SandboxImplementationModule {
    @Override
    protected void configure() {
        super.configure();

        bind(SandboxPlatform.class).to(SandboxFabric.class);
    }
}
