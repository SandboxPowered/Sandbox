package org.sandboxpowered.loader.forge.inject;

import org.sandboxpowered.loader.forge.SandboxForgeCore;
import org.sandboxpowered.loader.inject.SandboxImplementationModule;
import org.sandboxpowered.loader.platform.SandboxPlatform;

public class ForgeImplementationModule extends SandboxImplementationModule {
    @Override
    protected void configure() {
        super.configure();

        bind(SandboxPlatform.class).to(SandboxForgeCore.class);
    }
}
