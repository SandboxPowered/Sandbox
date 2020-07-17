package org.sandboxpowered.sandbox.fabric.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    public static Logger LOG = LogManager.getFormatterLogger("Sandbox|Fabric");

    public static void info(String message) {
        LOG.info(message);
    }

    public static void info(String message, Object... objs) {
        LOG.info(message, objs);
    }

    public static void error(String message) {
        LOG.error(message);
    }

    public static void error(String message, Throwable e) {
        LOG.error(message, e);
    }

    public static void error(String message, Object... objs) {
        LOG.error(message, objs);
    }

    public static void warn(String message) {
        LOG.warn(message);
    }

    public static void warn(String message, Throwable e) {
        LOG.warn(message, e);
    }

    public static void warn(String message, Object... objs) {
        LOG.warn(message, objs);
    }

    public static void fatal(String message) {
        LOG.fatal(message);
    }

    public static void fatal(String message, Throwable e) {
        LOG.fatal(message, e);
    }

    public static void fatal(String message, Object... objs) {
        LOG.fatal(message, objs);
    }

    public static void debug(String message) {
        LOG.debug(message);
    }
}