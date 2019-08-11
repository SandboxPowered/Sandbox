package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.api.SandboxAPI;
import com.hrznstudio.sandbox.event.EventDispatcher;

public abstract class SandboxCommon implements SandboxAPI {

    protected EventDispatcher dispatcher;

    protected abstract void setup();

    public abstract void shutdown();

    public EventDispatcher getDispatcher() {
        return dispatcher;
    }
}
