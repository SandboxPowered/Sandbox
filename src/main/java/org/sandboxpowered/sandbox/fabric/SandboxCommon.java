package org.sandboxpowered.sandbox.fabric;

import net.fabricmc.loader.api.FabricLoader;
import org.sandboxpowered.api.SandboxAPI;
import org.sandboxpowered.api.addon.AddonInfo;
import org.sandboxpowered.api.server.Server;
import org.sandboxpowered.api.util.Log;
import org.sandboxpowered.sandbox.fabric.util.AddonLog;

public abstract class SandboxCommon implements SandboxAPI {

    public static Server server;
    private Log log = new AddonLog();

    @Override
    public AddonInfo getSourceAddon() {
        return null;
    }

    @Override
    public boolean isExternalModLoaded(String loader, String modId) {
        if (!"fabric".equals(loader))
            return false;
        if (modId == null || modId.isEmpty())
            return true;
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    protected abstract void setup();

    public abstract void shutdown();

    @Override
    public Log getLog() {
        return log;
    }
}