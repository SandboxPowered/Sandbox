package org.sandboxpowered.loader.loading;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sandboxpowered.api.util.Log;
import org.sandboxpowered.internal.AddonSpec;

public class AddonLog implements Log {
    private final AddonSpec spec;
    private final Logger logger;

    public AddonLog(AddonSpec spec) {
        this.spec = spec;
        this.logger = LogManager.getLogger(spec.getTitle());
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

    @Override
    public void info(String message, Object... args) {
        logger.info(message, args);
    }

    @Override
    public void error(String message, Object... args) {
        logger.error(message, args);
    }

    @Override
    public void debug(String message, Object... args) {
        logger.debug(message, args);
    }

    @Override
    public void error(String message, Throwable e) {
        logger.error(message, e);
    }
}