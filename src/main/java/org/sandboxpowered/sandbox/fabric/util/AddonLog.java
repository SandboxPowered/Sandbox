package org.sandboxpowered.sandbox.fabric.util;

import org.sandboxpowered.api.util.Log;

public class AddonLog implements Log {
    @Override
    public void info(String message) {
        org.sandboxpowered.sandbox.fabric.util.Log.info(message);
    }

    @Override
    public void error(String message) {
        org.sandboxpowered.sandbox.fabric.util.Log.error(message);
    }

    @Override
    public void debug(String message) {
        org.sandboxpowered.sandbox.fabric.util.Log.debug(message);
    }
}
