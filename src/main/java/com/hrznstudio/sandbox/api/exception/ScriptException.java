package com.hrznstudio.sandbox.api.exception;

public class ScriptException  {
    private final Exception exception;

    public ScriptException(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
