package com.hrznstudio.sandbox.api;

public interface IDownloadIndicator {
    long getCurrentSize();

    long getTotalSize();

    boolean isComplete();

    boolean hasStarted();
}
