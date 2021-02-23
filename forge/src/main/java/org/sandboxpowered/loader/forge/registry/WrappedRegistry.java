package org.sandboxpowered.loader.forge.registry;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.registry.Registry;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.loader.Wrappers;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class WrappedRegistry<S extends Content<S>, V extends IForgeRegistryEntry<V>> implements Registry<S> {
    private final Identity identity;
    //    private final Map<Identity, RegistryEntry<A>> cacheMap = new TreeMap<>(Comparator.comparing(Identity::getPath).thenComparing(Identity::getNamespace));
    private final IForgeRegistry<V> vanilla;
    private final Wrappers.Wrapper<S, V> wrapper;
    private int cacheIndex = 0;

    public WrappedRegistry(Identity identity, IForgeRegistry<V> vanilla, Wrappers.Wrapper<S, V> wrapper) {
        this.identity = identity;
        this.wrapper = wrapper;
        this.vanilla = vanilla;
    }

    @Override
    public Identity getIdentity(S val) {
        return Wrappers.IDENTITY.toSandbox(vanilla.getKey(wrapper.toVanilla(val)));
    }

    @Override
    public Entry<S> get(Identity identity) {
        //wrapper.toSandbox(vanilla.get(Wrappers.IDENTITY.toVanilla(identity)))
        //TODO create entry system
        return null;
    }

    @Override
    public Entry<S> register(S val) {
        vanilla.register(wrapper.toVanilla(val).setRegistryName(Wrappers.IDENTITY.toVanilla(val.getIdentity())));
        return get(val.getIdentity());
    }

    @Override
    public Stream<S> stream() {
        return vanilla.getValues().stream().map(wrapper::toSandbox);
    }

    @Override
    public Collection<Identity> keys() {
        return Wrappers.IDENTITY.toSandboxList(vanilla.getKeys());
    }

    @Override
    public Class<S> getType() {
        return wrapper.getSandboxType();
    }

    @Override
    public Identity getIdentity() {
        return identity;
    }

    public void resetCache() {
        cacheIndex++;
    }

    public static class RegistryEntry<T extends Content<T>> implements Entry<T> {
        private final Identity identity;
        private final WrappedRegistry<T, ?> registry;
        private int cacheIndex = -1;
        private T cache;

        public RegistryEntry(Identity identity, WrappedRegistry<T, ?> registry) {
            this.identity = identity;
            this.registry = registry;
        }

        @Override
        public boolean isPresent() {
            resetCachedValueIfApplicable();
            return cache != null;
        }

        @Override
        public T get() {
            resetCachedValueIfApplicable();
            if (cache == null)
                throw new NoSuchElementException("No such registered object " + identity);
            return cache;
        }

        @Override
        public Optional<T> getAsOptional() {
            resetCachedValueIfApplicable();
            return Optional.ofNullable(cache);
        }

        private void resetCachedValueIfApplicable() {
            if (registry.cacheIndex != cacheIndex) {
                cache = registry.getCurrent(identity);
                cacheIndex = registry.cacheIndex;
            }
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
        public @Nullable T orNull() {
            return getAsOptional().orElse(null);
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
        public void ifPresentOrElse(Consumer<T> tConsumer, Runnable notPresent) {
            Optional<T> tOptional = getAsOptional();
            if (tOptional.isPresent()) {
                tConsumer.accept(tOptional.get());
            } else {
                notPresent.run();
            }
        }
    }

    private S getCurrent(Identity identity) {
        return wrapper.toSandbox(vanilla.getValue(Wrappers.IDENTITY.toVanilla(identity)));
    }
}