package org.sandboxpowered.loader;

import com.google.inject.Inject;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.logging.log4j.Logger;
import org.sandboxpowered.loader.config.Config;
import org.sandboxpowered.loader.loading.AddonSecurityPolicy;
import org.sandboxpowered.loader.loading.SandboxLoader;
import org.sandboxpowered.loader.platform.SandboxPlatform;

import java.io.IOException;
import java.security.Policy;

public abstract class SandboxCore implements SandboxPlatform {
    protected Config config;
    @Inject
    protected Logger logger;

    protected SandboxLoader loader = new SandboxLoader();

    public SandboxCore() {
        try {
            config = new Config("data/sandbox/sandbox.toml");
        } catch (IOException e) {
            getLog().error("Error creating configuration file", e);
            return;
        }

        config.save();
    }

    public void load(LevelStorageSource.LevelStorageAccess storageSource) {
        if (loader.isLoaded()) {
            throw new UnsupportedOperationException("Cannot load Sandbox if in already loaded state");
        }
        loader.load(storageSource);
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

    public Logger getLog() {
        return logger;
    }
}