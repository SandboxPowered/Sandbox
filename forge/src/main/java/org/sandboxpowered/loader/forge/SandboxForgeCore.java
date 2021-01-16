package org.sandboxpowered.loader.forge;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.loader.CacheableRegistry;
import org.sandboxpowered.loader.SandboxCore;
import org.sandboxpowered.loader.Wrappers;

public class SandboxForgeCore extends SandboxCore {
    public static final SandboxForgeCore CORE = new SandboxForgeCore();

    @Override
    protected void initCachedRegistries() {
        setRegistryWrapper(ForgeRegistries.BLOCKS, Wrappers.BLOCK);
        setRegistryWrapper(ForgeRegistries.ITEMS, Wrappers.ITEM);
        setRegistryWrapper(ForgeRegistries.FLUIDS, Wrappers.FLUID);
        setRegistryWrapper(ForgeRegistries.ENCHANTMENTS, Wrappers.ENCHANTMENT);
    }

    private <X extends Content<X>, Y extends IForgeRegistryEntry<Y>> void setRegistryWrapper(IForgeRegistry<Y> registry, Wrappers.Wrapper<X, Y> wrapper) {
        ((CacheableRegistry<X, Y>) registry).setWrapper(wrapper);
    }

    @Override
    public Identity getIdentity() {
        return null;
    }
}