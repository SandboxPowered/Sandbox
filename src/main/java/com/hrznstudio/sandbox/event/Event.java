package com.hrznstudio.sandbox.event;

public class Event {
    protected boolean complete;

    void complete() {
        System.out.println("Completed Event "+toString());
        this.complete = true;
    }

    protected void validateChange() {
        if (complete) {
            throw new UnsupportedOperationException("Cannot set value on event in an asynchronous context");
        }
    }
}
