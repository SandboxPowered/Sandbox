package org.sandboxpowered.sandbox.fabric.impl;

import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import net.minecraft.util.registry.SimpleRegistry;
import org.sandboxpowered.sandbox.api.registry.Registry;
import org.sandboxpowered.sandbox.api.util.Identity;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class BasicRegistry<A, B> implements Registry<A> {
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

    public BasicRegistry(SimpleRegistry vanilla, Class type, Function convertAB, Function convertBA, boolean fuck) {
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
        return convertBA.apply(vanilla.get(WrappingUtil.convert(identity)));
    }

    @Override
    public void register(Identity identity, A val) {
        vanilla.add(WrappingUtil.convert(identity), convertAB.apply(val));
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