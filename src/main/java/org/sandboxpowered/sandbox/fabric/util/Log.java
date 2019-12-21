package org.sandboxpowered.sandbox.fabric.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    public static Logger LOG = LogManager.getFormatterLogger("Sandbox|Fabric");

    public static void info(String s) {
        LOG.info("[Sandbox] " + s);
    }

    public static void info(String s, Object... objs) {
        LOG.info("[Sandbox] " + s, objs);
    }

    public static void error(String s) {
        LOG.error("[Sandbox] " + s);
    }

    public static void error(String s, Throwable e) {
        LOG.error("[Sandbox] " + s, e);
    }

    public static void error(String s, Object... objs) {
        LOG.error("[Sandbox] " + s, objs);
    }

    public static void warn(String s) {
        LOG.warn("[Sandbox] " + s);
    }

    public static void warn(String s, Throwable e) {
        LOG.warn("[Sandbox] " + s, e);
    }

    public static void warn(String s, Object... objs) {
        LOG.warn("[Sandbox] " + s, objs);
    }

    public static void fatal(String s) {
        LOG.fatal("[Sandbox] " + s);
    }

    public static void fatal(String s, Throwable e) {
        LOG.fatal("[Sandbox] " + s, e);
    }

    public static void fatal(String s, Object... objs) {
        LOG.fatal("[Sandbox] " + s, objs);
    }

    public static void debug(String s) {
        LOG.info("[SandboxDebug] " + s);
    }
}