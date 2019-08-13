package com.hrznstudio.sandbox.api;

import com.hrznstudio.sandbox.impl.BasicRegistry;

public interface SandboxRegistry {
    interface Internal {
        void store();

        void reset();

        void set(BasicRegistry registry);

        BasicRegistry get();
    }
}
