package com.hrznstudio.sandbox.api;

import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import com.hrznstudio.sandbox.Sandbox;
import com.hrznstudio.sandbox.server.SandboxServer;
import com.hrznstudio.sandbox.api.exception.ScriptException;
import com.hrznstudio.sandbox.util.Log;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ScriptEngine {

    public V8 ENGINE;

    public NodeJS NODE;

    public List<V8Object> OBJECTS;

    public Map<String, V8Object> REQUIRE_MAP = new HashMap<>();

    public ScriptEngine() {
    }

    public void init(ISandbox sandbox) {
        Log.info("Starting V8 Runtime");
        ENGINE = V8.createV8Runtime();
        OBJECTS = new ArrayList<>();
        addObjects(sandbox);
        ENGINE.add("CLIENT", sandbox.getSide() == Side.CLIENT);
        ENGINE.add("SERVER", sandbox.getSide() == Side.SERVER);
    }

    private void addObjects(ISandbox sandbox) {
        ENGINE.add("Registry", createV8()
                .add("GAMEMODE", createRegistryObject(sandbox.getRegistry(SandboxRegistry.RegistryType.GAMEMODE)))
                .add("BLOCK", createRegistryObject(sandbox.getRegistry(SandboxRegistry.RegistryType.BLOCK)))
                .add("ITEM", createRegistryObject(sandbox.getRegistry(SandboxRegistry.RegistryType.ITEM)))
                .add("ENTITY", createRegistryObject(sandbox.getRegistry(SandboxRegistry.RegistryType.ENTITY)))
                .add("BLOCK_ENTITY", createRegistryObject(sandbox.getRegistry(SandboxRegistry.RegistryType.BLOCK_ENTITY)))
        );
        ENGINE.add("Log", createV8()
                .registerJavaMethod((receiver, parameters) -> {
                    if (parameters.getType(0) == V8Value.STRING)
                        Log.info(parameters.getString(0));
                }, "info")
                .registerJavaMethod((receiver, parameters) -> {
                    if (parameters.getType(0) == V8Value.STRING)
                        Log.error(parameters.getString(0));
                }, "error")
        );
        ENGINE.registerJavaMethod((receiver, parameters) -> {
            if (REQUIRE_MAP.containsKey(parameters.getString(0))) {
                return parameters.getString(0);
            } else {
                return null;
            }
        }, "require");
    }

    public V8Object createV8() {
        V8Object o = new V8Object(ENGINE);
        OBJECTS.add(o);
        return o;
    }

    private V8Object createRegistryObject(SandboxRegistry registry) {
        return createV8()
                .registerJavaMethod((receiver, parameters) -> {
                    return registry.get(new Identifier(parameters.getString(0)));
                }, "get")
                .registerJavaMethod((receiver, parameters) -> {
                    V8Object object = parameters.getObject(0);
                    OBJECTS.add(object);
                    String name = object.getString("name");
                    String extend = object.contains("extends") ? object.getString("extends") : "block";
                    SandboxServer.INSTANCE.loadBlock(new Identifier("sandbox", name), (Block) Sandbox.getReg(SandboxRegistry.RegistryType.BLOCK).get(extend).apply(object), ItemGroup.REDSTONE);
                }, "register");
    }

    public void shutdown() {
        OBJECTS.forEach(v -> {
            if (!v.isReleased())
                v.release();
        });
        ENGINE.release();
    }

    public Optional<ScriptException> executeScript(File script) {
        return executeScript(script, StandardCharsets.UTF_8);
    }

    public Optional<ScriptException> executeScript(File script, Charset charset) {
        try {
            return executeScript(FileUtils.readFileToString(script, charset));
        } catch (IOException e) {
            return Optional.of(new ScriptException(e));
        }
    }

    public Optional<ScriptException> executeScript(String script) {
        try {
            ENGINE.executeVoidScript(String.format("'use strict'; (function () {%s\n})();", script));
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(new ScriptException(e));
        }
    }
}