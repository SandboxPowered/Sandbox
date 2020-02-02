package org.sandboxpowered.sandbox.fabric.impl;

import net.minecraft.util.registry.SimpleRegistry;
import org.sandboxpowered.sandbox.api.content.Content;
import org.sandboxpowered.sandbox.api.registry.Registry;
import org.sandboxpowered.sandbox.api.util.Identity;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BasicRegistry<A extends Content<A>, B> implements Registry<A> {
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
    public Mono<A> get(Identity identity) {
        B b = vanilla.get(WrappingUtil.convert(identity));
        if (b == null)
            return Mono.empty();
        return Mono.of(convertBA.apply(b));
    }

    @Override
    public void register(Identity identity, A val) {
        vanilla.add(WrappingUtil.convert(identity), convertAB.apply(val));
    }

    @Override
    public Collection<A> values() {
        return vanilla.stream().map(convertBA).collect(Collectors.toList());
    }

    @Override
    public Stream<A> stream() {
        return vanilla.stream().map(convertBA);
    }

    @Override
    public Collection<Identity> keys() {
        return vanilla.getIds().stream().map(id -> (Identity) id).collect(Collectors.toList());
    }

    @Override
    public Class<A> getType() {
        return type;
    }
}