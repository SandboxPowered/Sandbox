package org.sandboxpowered.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sandboxpowered.loader.config.Config;

import java.io.IOException;

public abstract class SandboxCore {
    protected Config config;
    protected Logger logger;

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

    public void init() {
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