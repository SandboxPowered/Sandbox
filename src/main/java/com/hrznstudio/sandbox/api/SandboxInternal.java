package com.hrznstudio.sandbox.api;

import com.hrznstudio.sandbox.api.block.IBlock;
import com.hrznstudio.sandbox.api.item.IItem;
import com.hrznstudio.sandbox.impl.BasicRegistry;

public class SandboxInternal {

    public interface StateFactory {
        com.hrznstudio.sandbox.api.state.StateFactory getSboxFactory();

        void setSboxFactory(com.hrznstudio.sandbox.api.state.StateFactory factory);
    }
    public interface StateFactoryBuilder {
        com.hrznstudio.sandbox.api.state.StateFactory.Builder getSboxBuilder();

        void setSboxBuilder(com.hrznstudio.sandbox.api.state.StateFactory.Builder builder);
    }

    public interface Registry {
        void store();

        void reset();

        void set(BasicRegistry registry);

        BasicRegistry get();
    }

    public interface ItemWrapper {
        IItem getItem();
    }
    public interface BlockWrapper {
        IBlock getBlock();
    }
    public interface BaseFluid {
        boolean sandboxinfinite();
    }

    public interface WrappedInjection {
        Object getInjectionWrapped();

        void setInjectionWrapped(Object o);
    }
    public interface StateFactoryHolder {
        com.hrznstudio.sandbox.api.state.StateFactory getSandboxStateFactory();
    }
}