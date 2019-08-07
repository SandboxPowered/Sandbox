package com.hrznstudio.sandbox.api;

import net.minecraft.util.Identifier;

public interface SandboxRegistry<T> {

    void register(Identifier identifier, T object);

    T get(Identifier identifier);

    enum RegistryType {
        BLOCK,
        ITEM,
        ENTITY,
        BLOCK_ENTITY;
    }

    interface Internal {
        void store();

        void reset();
    }
}
