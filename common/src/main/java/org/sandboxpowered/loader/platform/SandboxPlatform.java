package org.sandboxpowered.loader.platform;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.loader.loading.SandboxLoader;

import java.io.IOException;

public interface SandboxPlatform {
    Identity getIdentity();

    void load(MinecraftServer server, LevelStorageSource.LevelStorageAccess storageSource) throws IOException;

    void unload() throws IOException;

    void init();

    boolean isLoaded();

    SandboxLoader getLoader();
}
