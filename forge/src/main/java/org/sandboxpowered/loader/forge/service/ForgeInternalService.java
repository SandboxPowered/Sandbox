package org.sandboxpowered.loader.forge.service;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.registry.Registry;
import org.sandboxpowered.loader.Wrappers;
import org.sandboxpowered.loader.forge.registry.WrappedRegistry;
import org.sandboxpowered.loader.service.BaseInternalService;

public class ForgeInternalService extends BaseInternalService {

    private static final Registry<Item> ITEM = createRegistry(ForgeRegistries.ITEMS, Wrappers.ITEM);
    private static final Registry<Block> BLOCK = createRegistry(ForgeRegistries.BLOCKS, Wrappers.BLOCK);

    @Override
    public <T extends Content<T>> Registry<T> registryFunction(Class<T> c) {
        if (c == Item.class)
            return (Registry<T>) ITEM;
        if (c == Block.class)
            return (Registry<T>) BLOCK;
        return null;
    }


    private static <V extends IForgeRegistryEntry<V>, S extends Content<S>> Registry<S> createRegistry(IForgeRegistry<V> registry, Wrappers.Wrapper<S, V> wrapper) {
        return new WrappedRegistry<>(Wrappers.IDENTITY.toSandbox(registry.getRegistryName()), registry, wrapper);
    }
}
