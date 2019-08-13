package com.hrznstudio.sandbox.impl;

import com.hrznstudio.sandbox.api.registry.Registry;
import com.hrznstudio.sandbox.api.util.Identity;
import com.hrznstudio.sandbox.util.ConversionUtil;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class BasicRegistry<A,B> implements Registry<A> {
    private final Function<A, B> convertAB;
    private final Function<B, A> convertBA;
    private final SimpleRegistry<B> vanilla;
    private final Class<A> type;

    public BasicRegistry(SimpleRegistry<B> vanilla, Class<A> type, Function<A, B> convertAB, Function<B, A> convertBA) {
        this.convertAB = convertAB;
        this.convertBA = convertBA;
        this.vanilla = vanilla;
        this.type = type;
    }

    @Override
    public Identity getIdentity(A val) {
        return (Identity) vanilla.getId(convertAB.apply(val));
    }

    @Override
    public A get(Identity identity) {
        return convertBA.apply(vanilla.get(ConversionUtil.convert(identity)));
    }

    @Override
    public void register(Identity identity, A val) {
        vanilla.add(ConversionUtil.convert(identity), convertAB.apply(val));
    }

    @Override
    public Collection<A> values() {
        return Collections.emptySet();
    }

    @Override
    public Collection<Identity> keys() {
        return Collections.emptySet();
    }

    @Override
    public Class<A> getType() {
        return type;
    }
}