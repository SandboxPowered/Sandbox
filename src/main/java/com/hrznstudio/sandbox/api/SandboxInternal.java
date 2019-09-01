package com.hrznstudio.sandbox.api;

import com.hrznstudio.sandbox.api.item.IItem;
import com.hrznstudio.sandbox.impl.BasicRegistry;
import com.hrznstudio.sandbox.util.wrapper.FluidComparability;

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

    public interface WrappedInjection {
        Object getInjectionWrapped();

        void setInjectionWrapped(Object o);
    }

    public interface FluidStateCompare {
        FluidComparability getComparability();
    }
}