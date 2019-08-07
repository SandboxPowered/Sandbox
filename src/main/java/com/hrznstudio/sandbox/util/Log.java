package com.hrznstudio.sandbox.util;

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

    public static void debug(String s) {
        LOG.debug("[SandboxDebug] " + s);
    }
}