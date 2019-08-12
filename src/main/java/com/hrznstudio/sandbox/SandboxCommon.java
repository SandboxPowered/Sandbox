package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.api.SandboxAPI;
import com.hrznstudio.sandbox.api.event.Event;
import com.hrznstudio.sandbox.api.event.GenericEvent;
import com.hrznstudio.sandbox.api.event.Priority;
import com.hrznstudio.sandbox.event.EventDispatcher;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class SandboxCommon implements SandboxAPI {

    protected EventDispatcher dispatcher;

    protected abstract void setup();

    public abstract void shutdown();

    public EventDispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public <T extends Event> void on(Class<T> event, Predicate<T> filter, Priority priority, boolean receiveCancelled, Consumer<T> consumer) {

    }

    @Override
    public <T extends GenericEvent<G>, G> void onGeneric(Class<G> filter, Priority priority, boolean receiveCancelled, Consumer<T> consumer) {

    }
}
