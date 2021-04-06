package org.sandboxpowered.loader.inject;

import org.apache.logging.log4j.Logger;
import org.sandboxpowered.api.inject.FactoryProvider;
import org.sandboxpowered.api.inject.Implementation;
import org.sandboxpowered.api.inject.ImplementationModule;
import org.sandboxpowered.api.inject.Sandbox;
import org.sandboxpowered.loader.platform.Platform;
import org.sandboxpowered.loader.platform.SandboxPlatform;
import org.sandboxpowered.loader.util.SandboxImpl;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public class SandboxImplementationModule extends ImplementationModule {
    @Override
    @OverridingMethodsMustInvokeSuper
    protected void configure() {
        super.configure();

        expose(SandboxPlatform.class);

        expose(FactoryProvider.class);

        bind(Logger.class).toInstance(SandboxImpl.LOGGER);

        bind(FactoryProvider.class).to(SandboxFactoryProvider.class);
        bind(Implementation.class).to(SandboxImplementation.class);

        requestStaticInjection(Sandbox.class);
        requestStaticInjection(Platform.class);
    }
}
