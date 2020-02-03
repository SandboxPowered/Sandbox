package org.sandboxpowered.sandbox.fabric.impl;

import net.minecraft.util.registry.SimpleRegistry;
import org.sandboxpowered.sandbox.api.content.Content;
import org.sandboxpowered.sandbox.api.registry.Registry;
import org.sandboxpowered.sandbox.api.util.Identity;
import org.sandboxpowered.sandbox.api.util.Mono;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BasicRegistry<A extends Content<A>, B> implements Registry<A> {
    private final Map<Identity, RegistryEntry<A>> cacheMap = new TreeMap<>(Comparator.comparing(Identity::getPath).thenComparing(Identity::getNamespace));
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
    public Entry<A> get(Identity identity) {
        return cacheMap.computeIfAbsent(identity, id -> new RegistryEntry<>(id, this));
    }

    public A getCurrent(Identity identity) {
        return convertBA.apply(vanilla.get(WrappingUtil.convert(identity)));
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

    public void clearCache() {
        cacheMap.forEach((identity, aEntry) -> aEntry.clearCache());
    }

    public static class RegistryEntry<T extends Content<T>> implements Entry<T> {
        private Identity identity;
        private BasicRegistry<T, ?> registry;
        private boolean hasCached;
        private T cache;

        public RegistryEntry(Identity identity, BasicRegistry<T, ?> registry) {
            this.identity = identity;
            this.registry = registry;
        }

        @Override
        public boolean isPresent() {
            if (!hasCached) {
                cache = registry.getCurrent(identity);
                hasCached = true;
            }
            return cache != null;
        }

        @Override
        public T get() throws NoSuchElementException {
            if (!hasCached) {
                cache = registry.getCurrent(identity);
                hasCached = true;
            }
            if (cache == null)
                throw new NoSuchElementException("No such registered object " + identity);
            return cache;
        }

        @Override
        public Optional<T> getAsOptional() {
            if (!hasCached) {
                cache = registry.getCurrent(identity);
                hasCached = true;
            }
            return Optional.ofNullable(cache);
        }

        public void clearCache() {
            cache = null;
            hasCached = false;
        }

        @Override
        public boolean matches(T other) {
            return getAsOptional().map(a -> a == other).orElse(false);
        }

        @Override
        public T orElse(T other) {
            return getAsOptional().orElse(other);
        }

        @Override
        public T orElseGet(Supplier<T> other) {
            return getAsOptional().orElseGet(other);
        }

        @Override
        public void ifPresent(Consumer<T> tConsumer) {
            getAsOptional().ifPresent(tConsumer);
        }

        @Override
        public void ifPresent(Consumer<T> tConsumer, Runnable notPresent) {
            Optional<T> tOptional = getAsOptional();
            if (tOptional.isPresent()) {
                tConsumer.accept(tOptional.get());
            } else {
                notPresent.run();
            }
        }
    }
}