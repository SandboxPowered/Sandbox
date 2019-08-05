package com.hrznstudio.sandbox.test;

import com.hrznstudio.sandbox.event.Event;
import com.hrznstudio.sandbox.event.EventDispatcher;

import java.time.Duration;

public class TestAddon {
    public TestAddon() {
        EventDispatcher.getServerDispatcher().on(Event.class).delayElements(Duration.ofSeconds(10)).subscribe(event -> {
            System.out.println("Event Received");
        });
    }
}