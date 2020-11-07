package org.sandboxpowered.loader;

import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.registry.Registry;
import org.sandboxpowered.api.util.Identity;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

public class WrappedRegistry<A extends Content<A>, B> implements Registry<A> {
    private final Identity identity;
//    private final Map<Identity, RegistryEntry<A>> cacheMap = new TreeMap<>(Comparator.comparing(Identity::getPath).thenComparing(Identity::getNamespace));
    private final Function<A, B> convertAB;
    private final Function<B, A> convertBA;
    private final net.minecraft.core.Registry<B> vanilla;
    private final Class<A> type;

    public WrappedRegistry(Identity identity, net.minecraft.core.Registry<B> vanilla, Class<A> type, Function<A, B> convertAB, Function<B, A> convertBA) {
        this.identity = identity;
        this.convertAB = convertAB;
        this.convertBA = convertBA;
        this.vanilla = vanilla;
        this.type = type;
    }

    @Override
    public Identity getIdentity(A val) {
        return null;
    }

    @Override
    public Entry<A> get(Identity identity) {
        return null;
    }

    @Override
    public Entry<A> register(A val) {
        return null;
    }

    @Override
    public Collection<A> values() {
        return null;
    }

    @Override
    public Stream<A> stream() {
        return null;
    }

    @Override
    public Collection<Identity> keys() {
        return null;
    }

    @Override
    public Class<A> getType() {
        return null;
    }

    @Override
    public Identity getIdentity() {
        return null;
    }
}