package com.hrznstudio.sandbox.event;

import com.hrznstudio.sandbox.util.ClassUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Event {
    private final boolean cancellable = ClassUtil.isAnnotationPresent(getClass(), Cancellable.class);
    private final boolean async = ClassUtil.isAnnotationPresent(getClass(), Async.class);
    protected boolean complete;
    private boolean cancelled;

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

    public boolean isAsync() {
        return async;
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