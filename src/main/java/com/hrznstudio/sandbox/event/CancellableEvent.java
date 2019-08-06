package com.hrznstudio.sandbox.event;

public class CancellableEvent extends Event {
    private boolean cancelled;

    public void cancel() {
        validateChange();
        cancelled = true;
    }

    public boolean wasCancelled() {
        return cancelled;
    }
}
