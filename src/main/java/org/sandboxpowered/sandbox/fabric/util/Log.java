package org.sandboxpowered.sandbox.fabric.util;

import io.sentry.event.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;

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
        SentryUtil.record(SentryUtil.create(s, Event.Level.ERROR));
    }

    public static void error(String s, Throwable e) {
        LOG.error("[Sandbox] " + s, e);
        SentryUtil.record(SentryUtil.create(s, Event.Level.ERROR, e));
    }

    public static void error(String s, Object... objs) {
        LOG.error("[Sandbox] " + s, objs);
        SentryUtil.record(SentryUtil.create(new ParameterizedMessage(s, objs).getFormattedMessage(), Event.Level.ERROR));
    }

    public static void warn(String s) {
        LOG.warn("[Sandbox] " + s);
        SentryUtil.record(SentryUtil.create(s, Event.Level.WARNING));
    }

    public static void warn(String s, Throwable e) {
        LOG.warn("[Sandbox] " + s, e);
        SentryUtil.record(SentryUtil.create(s, Event.Level.WARNING, e));
    }

    public static void warn(String s, Object... objs) {
        LOG.warn("[Sandbox] " + s, objs);
        SentryUtil.record(SentryUtil.create(new ParameterizedMessage(s, objs).getFormattedMessage(), Event.Level.WARNING));
    }

    public static void fatal(String s) {
        LOG.fatal("[Sandbox] " + s);
        SentryUtil.record(SentryUtil.create(s, Event.Level.FATAL));
    }

    public static void fatal(String s, Throwable e) {
        LOG.fatal("[Sandbox] " + s, e);
        SentryUtil.record(SentryUtil.create(s, Event.Level.FATAL, e));
    }

    public static void fatal(String s, Object... objs) {
        LOG.fatal("[Sandbox] " + s, objs);
        SentryUtil.record(SentryUtil.create(new ParameterizedMessage(s, objs).getFormattedMessage(), Event.Level.FATAL));
    }

    public static void debug(String s) {
        LOG.info("[SandboxDebug] " + s);
    }
}