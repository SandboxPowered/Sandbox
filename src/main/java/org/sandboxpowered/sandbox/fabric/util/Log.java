package org.sandboxpowered.sandbox.fabric.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    public static final Logger LOGGER = LogManager.getFormatterLogger("Sandbox|Fabric");

    private Log() {
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void info(String message, Object... objs) {
        LOGGER.info(message, objs);
    }

    public static void error(String message) {
        LOGGER.error(message);
    }

    public static void error(String message, Throwable e) {
        LOGGER.error(message, e);
    }

    public static void error(String message, Object... objs) {
        LOGGER.error(message, objs);
    }

    public static void warn(String message) {
        LOGGER.warn(message);
    }

    public static void warn(String message, Throwable e) {
        LOGGER.warn(message, e);
    }

    public static void warn(String message, Object... objs) {
        LOGGER.warn(message, objs);
    }

    public static void fatal(String message) {
        LOGGER.fatal(message);
    }

    public static void fatal(String message, Throwable e) {
        LOGGER.fatal(message, e);
    }

    public static void fatal(String message, Object... objs) {
        LOGGER.fatal(message, objs);
    }

    public static void debug(String message) {
        LOGGER.info(message);
    }
}