package com.hrznstudio.sandbox.api;


import javax.script.ScriptEngineManager;

public class ScriptEngine {
    private static javax.script.ScriptEngine ENGINE;

    public static void init(ISandbox sandbox) {
        ENGINE = new ScriptEngineManager().getEngineByName("nashorn");
        ENGINE.put("CLIENT", sandbox.getSide() == Side.CLIENT);
        ENGINE.put("SERVER", sandbox.getSide() == Side.SERVER);
    }
}