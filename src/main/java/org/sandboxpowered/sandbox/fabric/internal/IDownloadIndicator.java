package org.sandboxpowered.sandbox.fabric.internal;

public interface IDownloadIndicator {
    long getCurrentSize();

    long getTotalSize();

    boolean isComplete();

    boolean hasStarted();
}
