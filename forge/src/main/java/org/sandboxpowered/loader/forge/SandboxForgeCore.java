package org.sandboxpowered.loader.forge;

import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.enchantment.Enchantment;
import org.sandboxpowered.api.fluid.Fluid;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.loader.CacheableRegistry;
import org.sandboxpowered.loader.SandboxCore;
import org.sandboxpowered.loader.Wrappers;

public class SandboxForgeCore extends SandboxCore {
    public static final SandboxForgeCore CORE = new SandboxForgeCore();

    @Override
    protected Logger createLogger() {
        return LogManager.getLogger("Sandbox|Forge");
    }

    @Override
    protected void initCachedRegistries() {
        ((CacheableRegistry<Block, net.minecraft.block.Block>) ForgeRegistries.BLOCKS).setWrapper(Wrappers.BLOCK);
        ((CacheableRegistry<Item, net.minecraft.item.Item>) ForgeRegistries.ITEMS).setWrapper(Wrappers.ITEM);
        ((CacheableRegistry<Fluid, net.minecraft.fluid.Fluid>) ForgeRegistries.FLUIDS).setWrapper(Wrappers.FLUID);
        ((CacheableRegistry<Enchantment, net.minecraft.enchantment.Enchantment>) ForgeRegistries.ENCHANTMENTS).setWrapper(Wrappers.ENCHANTMENT);
    }
}