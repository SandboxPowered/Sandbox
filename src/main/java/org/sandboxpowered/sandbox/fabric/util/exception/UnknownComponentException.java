package org.sandboxpowered.sandbox.fabric.util.exception;

public class UnknownComponentException extends RuntimeException {
    public UnknownComponentException(Class<?> xClass) {
        super("Unknown component " + xClass);
    }
}
