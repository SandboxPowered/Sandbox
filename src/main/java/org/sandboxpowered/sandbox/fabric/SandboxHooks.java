package org.sandboxpowered.sandbox.fabric;

import com.google.common.collect.Sets;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.enchantment.Enchantment;
import org.sandboxpowered.api.entity.Entity;
import org.sandboxpowered.api.fluid.Fluid;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.util.Side;
import org.sandboxpowered.sandbox.fabric.client.SandboxClient;
import org.sandboxpowered.sandbox.fabric.impl.BasicRegistry;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.security.AddonSecurityPolicy;
import org.sandboxpowered.sandbox.fabric.server.SandboxServer;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

import java.security.Policy;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class SandboxHooks {
    public static void shutdown() {
        if (Sandbox.SANDBOX.getSide() == Side.CLIENT) {
            SandboxClient.INSTANCE.shutdown();
            if (SandboxServer.INSTANCE != null && SandboxServer.INSTANCE.isIntegrated())
                SandboxServer.INSTANCE.shutdown();
        } else {
            SandboxServer.INSTANCE.shutdown();
        }
    }

    public static void setupGlobal() {
        Policy.setPolicy(new AddonSecurityPolicy());

        ((SandboxInternal.Registry) Registry.BLOCK).set(new BasicRegistry<>(Identity.of("block"), Registry.BLOCK, Block.class, WrappingUtil::convert, WrappingUtil::convert));
        ((SandboxInternal.Registry) Registry.ITEM).set(new BasicRegistry<>(Identity.of("item"), Registry.ITEM, Item.class, WrappingUtil::convert, WrappingUtil::convert));
        ((SandboxInternal.Registry) Registry.ENCHANTMENT).set(new BasicRegistry<>(Identity.of("enchantment"), (SimpleRegistry<net.minecraft.enchantment.Enchantment>) Registry.ENCHANTMENT, Enchantment.class, WrappingUtil::convert, b -> (Enchantment) b));
        ((SandboxInternal.Registry) Registry.FLUID).set(new BasicRegistry<>(Identity.of("fluid"), Registry.FLUID, Fluid.class, WrappingUtil::convert, WrappingUtil::convert));
        ((SandboxInternal.Registry) Registry.ENTITY_TYPE).set(new BasicRegistry<>(Identity.of("entity_type"), Registry.ENTITY_TYPE, Entity.Type.class, WrappingUtil::convert, WrappingUtil::convert));
        ((SandboxInternal.Registry) Registry.BLOCK_ENTITY_TYPE).set(new BasicRegistry<>(Identity.of("block_entity_type"), (SimpleRegistry) Registry.BLOCK_ENTITY_TYPE, BlockEntity.Type.class, (Function<BlockEntity.Type, BlockEntityType>) WrappingUtil::convert, (Function<BlockEntityType, BlockEntity.Type>) WrappingUtil::convert, true));
    }

    public static void shutdownGlobal() {
    }
}