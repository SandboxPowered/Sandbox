package org.sandboxpowered.sandbox.fabric;

import org.sandboxpowered.sandbox.api.SandboxAPI;
import org.sandboxpowered.sandbox.api.server.Server;
import org.sandboxpowered.sandbox.api.util.Log;
import org.sandboxpowered.sandbox.fabric.util.AddonLog;

public abstract class SandboxCommon implements SandboxAPI {

    public static Server server;
    private Log log = new AddonLog();

    protected abstract void setup();

    public abstract void shutdown();

    @Override
    public Log getLog() {
        return log;
    }
}