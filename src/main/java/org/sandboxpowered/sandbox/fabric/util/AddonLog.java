package org.sandboxpowered.sandbox.fabric.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sandboxpowered.api.addon.AddonInfo;
import org.sandboxpowered.api.util.Log;

public class AddonLog implements Log {
    private final AddonInfo info;
    private final Logger logger;

    public AddonLog(AddonInfo info) {
        this.info = info;
        logger = LogManager.getFormatterLogger(info.getTitle());
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public void debug(String message) {
        logger.debug(message);
    }
}
