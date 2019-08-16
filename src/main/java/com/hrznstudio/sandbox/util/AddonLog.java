package com.hrznstudio.sandbox.util;

import com.hrznstudio.sandbox.api.util.Log;

public class AddonLog implements Log {
    @Override
    public void info(String message) {
        com.hrznstudio.sandbox.util.Log.info(message);
    }

    @Override
    public void error(String message) {
        com.hrznstudio.sandbox.util.Log.error(message);
    }

    @Override
    public void debug(String message) {
        com.hrznstudio.sandbox.util.Log.debug(message);
    }
}
