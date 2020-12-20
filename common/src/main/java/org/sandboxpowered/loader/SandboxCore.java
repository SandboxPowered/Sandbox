package org.sandboxpowered.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sandboxpowered.loader.config.Config;
import org.sandboxpowered.loader.loading.AddonSecurityPolicy;
import org.sandboxpowered.loader.loading.SandboxLoader;
import org.sandboxpowered.loader.platform.SandboxPlatform;

import java.io.IOException;
import java.security.Policy;

public abstract class SandboxCore implements SandboxPlatform {
    protected Config config;
    protected Logger logger;

    protected SandboxLoader loader = new SandboxLoader();

    public SandboxCore() {
        this.logger = createLogger();
        try {
            config = new Config("data/sandbox/sandbox.toml");
        } catch (IOException e) {
            getLog().error("Error creating configuration file", e);
            return;
        }

        config.save();
    }

    public void load() {
        if (loader.isLoaded()) {
            throw new UnsupportedOperationException("Cannot load Sandbox if in already loaded state");
        }
        loader.load();
    }

    public void unload() {
        if (!loader.isLoaded()) {
            throw new UnsupportedOperationException("Cannot unload Sandbox if in unloaded state");
        }
        loader.unload();
    }

    public void init() {
        Policy.setPolicy(new AddonSecurityPolicy());
        initCachedRegistries();
    }

    protected abstract void initCachedRegistries();

    protected Logger createLogger() {
        return LogManager.getLogger("Sandbox");
    }

    public Logger getLog() {
        return logger;
    }
}