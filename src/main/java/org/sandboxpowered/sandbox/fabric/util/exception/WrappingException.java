package org.sandboxpowered.sandbox.fabric.util.exception;

public class WrappingException extends RuntimeException {
    public WrappingException(String message) {
        super(message);
    }

    public WrappingException(Class<?> clazz) {
        super(String.format("Unacceptable class '%s'", clazz));
    }
}
