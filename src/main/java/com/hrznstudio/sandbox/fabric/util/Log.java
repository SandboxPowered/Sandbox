package com.hrznstudio.sandbox.fabric.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private static Logger LOGGER = LogManager.getFormatterLogger("Sandbox|Fabric");

    public static void info(String s) {
        LOGGER.info("[Sandbox] " + s);
    }

    public static void error(String s) {
        LOGGER.error("[Sandbox] " + s);
    }
}
