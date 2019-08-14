package com.hrznstudio.sandbox;

import com.hrznstudio.sandbox.api.Registries;
import com.hrznstudio.sandbox.api.SandboxAPI;
import com.hrznstudio.sandbox.api.SandboxRegistry;
import com.hrznstudio.sandbox.api.block.Block;
import com.hrznstudio.sandbox.api.event.Event;
import com.hrznstudio.sandbox.api.event.Priority;
import com.hrznstudio.sandbox.api.item.Item;
import com.hrznstudio.sandbox.api.registry.Registry;
import com.hrznstudio.sandbox.event.EventDispatcher;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class SandboxCommon implements SandboxAPI, Registries {

    protected EventDispatcher dispatcher;

    protected abstract void setup();

    public abstract void shutdown();

    public EventDispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public <T extends Event> void on(Class<T> event, Predicate<T> filter, Priority priority, boolean receiveCancelled, Consumer<T> consumer) {

    }

    @Override
    public Registry<Block> getBlockRegistry() {
        return ((SandboxRegistry.Internal) net.minecraft.util.registry.Registry.BLOCK).get();
    }

    @Override
    public Registry<Item> getItemRegistry() {
        return ((SandboxRegistry.Internal) net.minecraft.util.registry.Registry.ITEM).get();
    }
}