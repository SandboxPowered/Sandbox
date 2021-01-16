package org.sandboxpowered.loader.platform;

import net.minecraft.world.level.storage.LevelStorageSource;
import org.sandboxpowered.api.util.Identity;

import java.io.IOException;

public interface SandboxPlatform {
    Identity getIdentity();

    void load(LevelStorageSource.LevelStorageAccess storageSource) throws IOException;

    void unload() throws IOException;

    void init();
}
