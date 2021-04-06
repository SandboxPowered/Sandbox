package org.sandboxpowered.loader.fabric;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.StairsShape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.loader.CacheableRegistry;
import org.sandboxpowered.loader.SandboxCore;
import org.sandboxpowered.loader.Wrappers;

public class SandboxFabric extends SandboxCore {
    private final Identity ID = Identity.of("sandbox", "fabric");

    @Override
    public Identity getIdentity() {
        return ID;
    }

    @Override
    protected void initCachedRegistries() {
        setRegistryWrapper(Registry.BLOCK, Wrappers.BLOCK);
        setRegistryWrapper(Registry.ITEM, Wrappers.ITEM);
        setRegistryWrapper(Registry.FLUID, Wrappers.FLUID);
        setRegistryWrapper(Registry.ENCHANTMENT, Wrappers.ENCHANTMENT);
    }

    private <X extends Content<X>, Y> void setRegistryWrapper(Registry<Y> registry, Wrappers.Wrapper<X, Y> wrapper) {
        ((CacheableRegistry<X, Y>) registry).setWrapper(wrapper);
    }
}
