package org.sandboxpowered.sandbox.fabric.util.wrapper;

import org.sandboxpowered.api.state.StateFactory;
import org.sandboxpowered.api.state.property.Property;
import org.sandboxpowered.api.state.property.PropertyContainer;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StateFactoryImpl<T, S extends PropertyContainer<S>, V, A extends net.minecraft.state.State<V, A>> implements StateFactory<T, S> {
    private final net.minecraft.state.StateManager<V, A> vanilla;
    private final Function<V, T> vTS;
    private final Function<A, S> aTS;

    public StateFactoryImpl(net.minecraft.state.StateManager<V, A> vanilla, Function<V, T> vTS, Function<A, S> aTS) {
        this.vanilla = vanilla;
        this.vTS = vTS;
        this.aTS = aTS;
    }

    @Override
    public S getBaseState() {
        return aTS.apply(vanilla.getDefaultState());
    }

    @Override
    public T getBaseObject() {
        return vTS.apply(vanilla.getOwner());
    }

    @Override
    public Collection<S> getValidStates() {
        return vanilla.getStates().stream().map(aTS).collect(Collectors.toList());
    }

    public static class BuilderImpl<T, S extends PropertyContainer<S>, V, A extends net.minecraft.state.State<V, A>> implements StateFactory.Builder<T, S> {
        private final net.minecraft.state.StateManager.Builder<V, A> vaBuilder;

        public BuilderImpl(net.minecraft.state.StateManager.Builder<V, A> vaBuilder) {
            this.vaBuilder = vaBuilder;
        }

        public Builder<T, S> add(Property<?>... properties) {
            for (Property<?> property : properties)
                vaBuilder.add(WrappingUtil.convert(property));
            return this;
        }
    }
}