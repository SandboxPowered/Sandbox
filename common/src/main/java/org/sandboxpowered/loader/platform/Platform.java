package org.sandboxpowered.loader.platform;

import com.google.inject.Inject;

public class Platform {
    @Inject
    private static SandboxPlatform platform;

    public static SandboxPlatform getPlatform() {
        if (platform == null) {
            throw new IllegalStateException("Sandbox has not been initialized!");
        }
        return platform;
    }
}
