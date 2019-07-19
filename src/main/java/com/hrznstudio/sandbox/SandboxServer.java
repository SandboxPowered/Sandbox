package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.api.addon.AddonInfo;

import java.util.Collections;
import java.util.List;

public class SandboxServer extends SandboxCommon {
    public static SandboxServer INSTANCE;
    private final boolean isIntegrated;
    public List<AddonInfo> ADDONS = Collections.emptyList();

    private SandboxServer(boolean isIntegrated) {
        this.isIntegrated = isIntegrated;
        INSTANCE = this;
    }

    public static SandboxServer constructAndSetup(boolean integrated) {
        SandboxServer server = new SandboxServer(integrated);
        server.setup();
        return server;
    }

    @Override
    protected void setup() {
        engine.init(Sandbox.SANDBOX);
        //Init server engine
    }

    public boolean isIntegrated() {
        return isIntegrated;
    }

    @Override
    public void shutdown() {
        super.shutdown();
        INSTANCE = null;
    }
}
