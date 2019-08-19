package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.api.block.state.PropertyContainer;
import com.hrznstudio.sandbox.api.block.state.StateFactory;

import java.util.function.Function;

public class StateFactoryImpl<T,S extends PropertyContainer<S>,V,A extends net.minecraft.state.PropertyContainer<A>> implements StateFactory<T,S> {
    private net.minecraft.state.StateFactory<V, A> vanilla;
    private Function<V, T> vTS;
    private Function<A, S> aTS;

    public StateFactoryImpl(net.minecraft.state.StateFactory<V, A> vanilla, Function<V, T> vTS, Function<A, S> aTS) {
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
        return vTS.apply(vanilla.getBaseObject());
    }
}
