package org.sandboxpowered.loader.fabric.service;

import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.registry.Registry;
import org.sandboxpowered.loader.Wrappers;
import org.sandboxpowered.loader.fabric.registry.WrappedRegistry;
import org.sandboxpowered.loader.service.BaseInternalService;

public class FabricInternalService extends BaseInternalService {

    private static final WrappedRegistry<Item, net.minecraft.world.item.Item> ITEM = createRegistry(net.minecraft.core.Registry.ITEM, Wrappers.ITEM);
    private static final WrappedRegistry<Block, net.minecraft.world.level.block.Block> BLOCK = createRegistry(net.minecraft.core.Registry.BLOCK, Wrappers.BLOCK);

    @Override
    public <T extends Content<T>> Registry<T> registryFunction(Class<T> c) {
        if (c == Item.class)
            return (Registry<T>) ITEM;
        if (c == Block.class)
            return (Registry<T>) BLOCK;
        return null;
    }

    private static <S extends Content<S>, V> WrappedRegistry<S, V> createRegistry(net.minecraft.core.WritableRegistry<V> registry, Wrappers.Wrapper<S, V> wrapper) {
        return new WrappedRegistry<>(
                Wrappers.IDENTITY.toSandbox(registry.key().location()),
                registry,
                wrapper
        );
    }

}
