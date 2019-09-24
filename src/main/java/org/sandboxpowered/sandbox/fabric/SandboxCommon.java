package org.sandboxpowered.sandbox.fabric;

import org.sandboxpowered.sandbox.api.SandboxAPI;
import org.sandboxpowered.sandbox.api.client.Client;
import org.sandboxpowered.sandbox.api.event.Event;
import org.sandboxpowered.sandbox.api.event.Priority;
import org.sandboxpowered.sandbox.api.server.Server;
import org.sandboxpowered.sandbox.api.util.Log;
import org.sandboxpowered.sandbox.fabric.event.EventDispatcher;
import org.sandboxpowered.sandbox.fabric.util.AddonLog;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class SandboxCommon implements SandboxAPI {

    public static Client client;
    public static Server server;
    private Log log = new AddonLog();

    protected abstract void setup();

    public abstract void shutdown();

    @Override
    public <T extends Event> void on(Class<T> event, Predicate<T> filter, Priority priority, boolean receiveCancelled, Consumer<T> consumer) {
        EventDispatcher.on(event, ev -> {
            if (filter.test(ev) && (!ev.isCancelled() || receiveCancelled)) consumer.accept(ev);
        }); //TODO: Respect other params
    }

    @Override
    public Log getLog() {
        return log;
    }
}