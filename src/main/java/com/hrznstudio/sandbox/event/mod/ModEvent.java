package com.hrznstudio.sandbox.event.mod;

import com.hrznstudio.sandbox.event.Event;

public class ModEvent extends Event {
    @Event.Async
    public static class Init extends ModEvent {
    }
}
