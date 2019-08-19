package com.hrznstudio.sandbox.api;

import com.hrznstudio.sandbox.impl.BasicRegistry;

public class SandboxInternal {

    public interface StateFactory {
        com.hrznstudio.sandbox.api.block.state.StateFactory getSboxFactory();
        void setSboxFactory(com.hrznstudio.sandbox.api.block.state.StateFactory factory);
    }

    public interface Registry {
        void store();

        void reset();

        void set(BasicRegistry registry);

        BasicRegistry get();
    }
}
