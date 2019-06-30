package com.hrznstudio.sandbox.api;

import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import com.hrznstudio.sandbox.util.Log;

public class ScriptEngine {

    public static V8 ENGINE;

    public static V8Object REGISTRY;

    public static void init(ISandbox sandbox) {
        Log.info("Starting V8 Runtime");
        ENGINE = NodeJS.createNodeJS().getRuntime();
        addObjects(sandbox);
        ENGINE.add("CLIENT", sandbox.getSide() == Side.CLIENT);
        ENGINE.add("SERVER", sandbox.getSide() == Side.SERVER);
    }

    private static void addObjects(ISandbox sandbox) {
        REGISTRY = new V8Object(ENGINE)
                .registerJavaMethod((receiver, parameters) -> {
                    String registryString = parameters.getString(0);
                    SandboxRegistry registry = sandbox.getRegistry(registryString);
                    //TODO
                }, "register")
                .add("BLOCK", "block")
                .add("ITEM", "item")
                .add("ENTITY", "entity")
                .add("TILE", "tileentity")
        ;
        ENGINE.add("Registry", REGISTRY);
    }
}