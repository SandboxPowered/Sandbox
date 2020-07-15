package org.sandboxpowered.sandbox.fabric.internal;

import net.minecraft.util.registry.RegistryKey;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.sandbox.fabric.impl.BasicRegistry;

public class SandboxInternal {

    public interface StateFactory {
        org.sandboxpowered.api.state.StateFactory getSboxFactory();

        void setSboxFactory(org.sandboxpowered.api.state.StateFactory factory);
    }

    public interface StateFactoryBuilder {
        org.sandboxpowered.api.state.StateFactory.Builder getSboxBuilder();

        void setSboxBuilder(org.sandboxpowered.api.state.StateFactory.Builder builder);
    }

    public interface Registry {
        void store();

        void reset();

        void set(BasicRegistry registry);

        BasicRegistry get();
    }

    public interface RegistryKeyObtainer<T> {
        RegistryKey<net.minecraft.util.registry.Registry<T>> sandbox_getRegistryKey();
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
        org.sandboxpowered.api.state.StateFactory getSandboxStateFactory();
    }
}