package org.sandboxpowered.loader.fabric;

import net.minecraft.core.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.enchantment.Enchantment;
import org.sandboxpowered.api.fluid.Fluid;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.loader.CacheableRegistry;
import org.sandboxpowered.loader.SandboxCore;
import org.sandboxpowered.loader.Wrappers;

public class SandboxFabric extends SandboxCore {
    public static SandboxFabric CORE = new SandboxFabric();

    @Override
    protected Logger createLogger() {
        return LogManager.getLogger("Sandbox|Fabric");
    }

    @Override
    protected void initCachedRegistries() {
        ((CacheableRegistry<Block, net.minecraft.world.level.block.Block>) Registry.BLOCK).setWrapper(Wrappers.BLOCK);
        ((CacheableRegistry<Item, net.minecraft.world.item.Item>) Registry.ITEM).setWrapper(Wrappers.ITEM);
        ((CacheableRegistry<Fluid, net.minecraft.world.level.material.Fluid>) Registry.FLUID).setWrapper(Wrappers.FLUID);
        ((CacheableRegistry<Enchantment, net.minecraft.world.item.enchantment.Enchantment>) Registry.ENCHANTMENT).setWrapper(Wrappers.ENCHANTMENT);
    }
}
