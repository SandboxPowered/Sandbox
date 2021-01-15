package org.sandboxpowered.loader.platform;

import org.sandboxpowered.api.util.Identity;

public interface SandboxPlatform {
    Identity getIdentity();

    void load();

    void unload();

    void init();
}
