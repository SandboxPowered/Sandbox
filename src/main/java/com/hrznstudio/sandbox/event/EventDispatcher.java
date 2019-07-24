package com.hrznstudio.sandbox.event;

public class EventDispatcher {

    public <E extends Event> void on(Class<E> eClass) {

    }

    public <E extends Event> E publish(E event) {


        return event;
    }
}