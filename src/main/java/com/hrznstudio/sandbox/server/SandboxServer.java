package com.hrznstudio.sandbox.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hrznstudio.sandbox.SandboxCommon;
import com.hrznstudio.sandbox.event.EventDispatcher;
import com.hrznstudio.sandbox.loader.SandboxLoader;
import com.hrznstudio.sandbox.util.Log;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;

public class SandboxServer extends SandboxCommon {
    private static final Gson GSON = new GsonBuilder().create();
    public static String[] ARGS;
    public static SandboxServer INSTANCE;
    private final boolean isIntegrated;
    private SandboxLoader loader;

    private SandboxServer(boolean isIntegrated) {
        this.isIntegrated = isIntegrated;
        INSTANCE = this;
    }

    public static SandboxServer constructAndSetup(boolean integrated) {
        SandboxServer server = new SandboxServer(integrated);
        server.setup();
        return server;
    }

    @Override
    protected void setup() {
        CONTENT_LIST.clear();
        Log.info("Setting up Serverside Sandbox environment");
        dispatcher = new EventDispatcher(
                EmitterProcessor.create(),
                Schedulers.parallel() // Server events are Async
        );
        load();
        if (!isIntegrated) {
            setupDedicated();
        }
    }

    protected void load() {
        loader = new SandboxLoader();
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void setupDedicated() {

    }

    public boolean isIntegrated() {
        return isIntegrated;
    }

    @Override
    public void shutdown() {
        super.shutdown();
        INSTANCE = null;
    }
}
