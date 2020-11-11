package org.sandboxpowered.loader.loading;

import org.sandboxpowered.api.SandboxAPI;
import org.sandboxpowered.api.addon.AddonInfo;
import org.sandboxpowered.api.util.Log;
import org.sandboxpowered.api.util.Side;
import org.sandboxpowered.internal.AddonSpec;

public class AddonSpecificAPIReference implements SandboxAPI {
    private final AddonSpec spec;
    private final SandboxLoader loader;
    private Log log;

    public AddonSpecificAPIReference(AddonSpec spec, SandboxLoader loader) {
        this.spec = spec;
        this.loader = loader;
    }

    @Override
    public boolean isAddonLoaded(String addonId) {
        return loader.isAddonLoaded(addonId);
    }

    @Override
    public boolean isExternalModLoaded(String loader, String modId) {
        return this.loader.isExternalModLoaded(loader, modId);
    }

    @Override
    public AddonInfo getSourceAddon() {
        return spec;
    }

    @Override
    public Side getSide() {
        return loader.getSide();
    }

    @Override
    public Log getLog() {
        if (log == null)
            log = new AddonLog(spec);
        return log;
    }
}
