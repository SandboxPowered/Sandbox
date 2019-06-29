package com.hrznstudio.sandbox.api;


import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

public class ScriptEngine {
    public static javax.script.ScriptEngine ENGINE;

    public static void init(ISandbox sandbox) {
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        ENGINE = factory.getScriptEngine("--no-java", "-language=es6");
        ENGINE.put("CLIENT", sandbox.getSide() == Side.CLIENT);
        ENGINE.put("SERVER", sandbox.getSide() == Side.SERVER);
    }
}