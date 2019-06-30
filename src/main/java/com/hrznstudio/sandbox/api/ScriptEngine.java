package com.hrznstudio.sandbox.api;

import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import com.hrznstudio.sandbox.util.Log;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

public class ScriptEngine {
    public static javax.script.ScriptEngine ENGINE;

    public static void init(ISandbox sandbox) {
        Log.info("Starting V8 Runtime");

        V8 runtime = NodeJS.createNodeJS().getRuntime();

        runtime.executeVoidScript("'use strict';"
                + "var person = {};\n" +
                "class Team {\n" +
                "  constructor() {\n" +
                "    this.name = \"Horizon\";\n" +
                "    this.info = \"\";\n" +
                "  }\n" +
                "  setInfo(info) {\n" +
                "    this.info = info;\n" +
                "  }\n" +
                "}"
                + "var team = new Team();\n"
                + "team.setInfo('We are the best team');\n"
                + "person.first = 'Dat';\n"
                + "person['last'] = 'Boi';\n"
                + "person.team = team;\n");
        // TODO: Access the person object
        V8Object person = runtime.getObject("person");
        V8Object hockeyTeam = person.getObject("team");
        System.out.println(hockeyTeam.getString("name"));
        person.release();
        hockeyTeam.release();
        runtime.release();

        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        ENGINE = factory.getScriptEngine("--no-java", "--language=es6");
        ENGINE.put("CLIENT", sandbox.getSide() == Side.CLIENT);
        ENGINE.put("SERVER", sandbox.getSide() == Side.SERVER);
    }
}