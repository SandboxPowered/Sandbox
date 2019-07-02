package com.hrznstudio.sandbox.api;

import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import com.hrznstudio.sandbox.api.exception.ScriptException;
import com.hrznstudio.sandbox.util.Log;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ScriptEngine {

    public static V8 ENGINE;

    public static NodeJS NODE;

    public static V8Object REGISTRY;

    public static Map<String, V8Object> REQUIRE_MAP = new HashMap<>();

    public static void init(ISandbox sandbox) {
        Log.info("Starting V8 Runtime");
        ENGINE = V8.createV8Runtime();
        addObjects(sandbox);
        ENGINE.add("CLIENT", sandbox.getSide() == Side.CLIENT);
        ENGINE.add("SERVER", sandbox.getSide() == Side.SERVER);
    }

    private static void addObjects(ISandbox sandbox) {
        ENGINE.add("Registry", new V8Object(ENGINE)
                .registerJavaMethod((receiver, parameters) -> {
                    String registryString = parameters.getString(0);
                    SandboxRegistry registry = sandbox.getRegistry(registryString);
                    V8Object object = parameters.getObject(1);
                    System.out.println(object);
                    //TODO
                }, "register")
                .add("BLOCK", "block")
                .add("ITEM", "item")
                .add("ENTITY", "entity")
                .add("TILE", "tileentity")
        );
        ENGINE.registerJavaMethod((receiver, parameters) -> {
            Log.info(parameters.getString(0));
        }, "print");
        ENGINE.registerJavaMethod((receiver, parameters) -> {
            if (REQUIRE_MAP.containsKey(parameters.getString(0))) {
                return parameters.getString(0);
            } else {
                return null;
            }
        }, "require");
    }

    public static void shutdown() {
        REGISTRY.release();
        ENGINE.release();
    }

    public static Optional<ScriptException> executeScript(File script) {
        return executeScript(script, StandardCharsets.UTF_8);
    }

    public static Optional<ScriptException> executeScript(File script, Charset charset) {
        try {
            return executeScript(FileUtils.readFileToString(script, charset));
        } catch (IOException e) {
            return Optional.of(new ScriptException(e));
        }
    }

    public static Optional<ScriptException> executeScript(String script) {
        try {
            ENGINE.executeVoidScript(String.format("'use strict'; (function () {%s\n})();", script));
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(new ScriptException(e));
        }
    }
}