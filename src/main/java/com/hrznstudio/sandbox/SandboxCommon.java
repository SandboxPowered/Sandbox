package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.api.SandboxAPI;
import com.hrznstudio.sandbox.api.client.Client;
import com.hrznstudio.sandbox.api.event.Event;
import com.hrznstudio.sandbox.api.event.Priority;
import com.hrznstudio.sandbox.api.server.Server;
import com.hrznstudio.sandbox.api.util.Log;
import com.hrznstudio.sandbox.event.EventDispatcher;
import com.hrznstudio.sandbox.util.AddonLog;

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