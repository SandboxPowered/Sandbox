package com.hrznstudio.sandbox.overlay;

import net.minecraft.resource.ResourceReloadMonitor;
import net.minecraft.util.Unit;

import java.util.concurrent.CompletableFuture;

//TODO: Actually track addon loading progress
public class AddonLoadingMonitor implements ResourceReloadMonitor {
    @Override
    public CompletableFuture<Unit> whenComplete() {
        return null;
    }

    @Override
    public float getProgress() {
        return 0.5f;
    }

    @Override
    public boolean isLoadStageComplete() {
        return true;
    }

    @Override
    public boolean isApplyStageComplete() {
        return true;
    }

    @Override
    public void throwExceptions() {

    }
}
