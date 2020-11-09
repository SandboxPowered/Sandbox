package org.sandboxpowered.loader.forge.registry;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.registry.Registry;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.loader.Wrappers;

import java.util.Collection;
import java.util.stream.Stream;

public class WrappedRegistry<S extends Content<S>, V extends IForgeRegistryEntry<V>> implements Registry<S> {
    private final Identity identity;
    //    private final Map<Identity, RegistryEntry<A>> cacheMap = new TreeMap<>(Comparator.comparing(Identity::getPath).thenComparing(Identity::getNamespace));
    private final IForgeRegistry<V> vanilla;
    private final Wrappers.Wrapper<S, V> wrapper;

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
}