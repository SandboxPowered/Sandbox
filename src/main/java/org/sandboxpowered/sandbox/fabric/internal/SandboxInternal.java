package org.sandboxpowered.sandbox.fabric.internal;

import org.sandboxpowered.sandbox.api.block.Block;
import org.sandboxpowered.sandbox.api.item.Item;
import org.sandboxpowered.sandbox.fabric.impl.BasicRegistry;

public class SandboxInternal {

    public interface StateFactory {
        org.sandboxpowered.sandbox.api.state.StateFactory getSboxFactory();

        void setSboxFactory(org.sandboxpowered.sandbox.api.state.StateFactory factory);
    }

    public interface StateFactoryBuilder {
        org.sandboxpowered.sandbox.api.state.StateFactory.Builder getSboxBuilder();

        void setSboxBuilder(org.sandboxpowered.sandbox.api.state.StateFactory.Builder builder);
    }

    public interface Registry {
        void store();

        void reset();

        void set(BasicRegistry registry);

        BasicRegistry get();
    }

    public interface ItemWrapper {
        Item getItem();
    }

    public interface BlockWrapper {
        Block getBlock();
    }
    public interface MaterialInternal {
        void sbxsetlevel(int level);
    }

    public interface BaseFluid {
        boolean sandboxinfinite();
    }

    public interface WrappedInjection {
        Object getInjectionWrapped();

        void setInjectionWrapped(Object o);
    }

    public interface StateFactoryHolder {
        org.sandboxpowered.sandbox.api.state.StateFactory getSandboxStateFactory();
    }
}