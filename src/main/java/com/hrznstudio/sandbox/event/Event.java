package com.hrznstudio.sandbox.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Event {
    protected boolean complete;
    private boolean cancelled;
    private boolean cancellable = getClass().isAnnotationPresent(Cancellable.class);

    public void cancel() {
        if (isCancellable()) {
            validateChange();
            cancelled = true;
        } else {
            throw new UnsupportedOperationException("Cannot cancel non-cancellable event");
        }
    }

    public boolean wasCancelled() {
        return cancelled;
    }

    public boolean isCancellable() {
        return cancellable;
    }

    void complete() {
        this.complete = true;
    }

    protected void validateChange() {
        if (complete) {
            throw new UnsupportedOperationException("Cannot set value on event in an asynchronous context");
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Async {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Cancellable {
    }
}