package org.sandboxpowered.sandbox.fabric;

import com.google.common.collect.Sets;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import org.sandboxpowered.sandbox.api.block.Block;
import org.sandboxpowered.sandbox.api.block.Material;
import org.sandboxpowered.sandbox.api.block.entity.BlockEntity;
import org.sandboxpowered.sandbox.api.client.Client;
import org.sandboxpowered.sandbox.api.client.render.RenderUtil;
import org.sandboxpowered.sandbox.api.component.Component;
import org.sandboxpowered.sandbox.api.container.ContainerFactory;
import org.sandboxpowered.sandbox.api.enchant.Enchantment;
import org.sandboxpowered.sandbox.api.entity.Entity;
import org.sandboxpowered.sandbox.api.fluid.Fluid;
import org.sandboxpowered.sandbox.api.item.Item;
import org.sandboxpowered.sandbox.api.item.ItemStack;
import org.sandboxpowered.sandbox.api.server.Server;
import org.sandboxpowered.sandbox.api.state.Property;
import org.sandboxpowered.sandbox.api.util.Functions;
import org.sandboxpowered.sandbox.api.util.Identity;
import org.sandboxpowered.sandbox.api.util.Side;
import org.sandboxpowered.sandbox.api.util.math.Position;
import org.sandboxpowered.sandbox.api.util.math.Vec3i;
import org.sandboxpowered.sandbox.api.util.nbt.CompoundTag;
import org.sandboxpowered.sandbox.api.util.text.Text;
import org.sandboxpowered.sandbox.fabric.client.SandboxClient;
import org.sandboxpowered.sandbox.fabric.impl.BasicRegistry;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.security.AddonSecurityPolicy;
import org.sandboxpowered.sandbox.fabric.server.SandboxServer;
import org.sandboxpowered.sandbox.fabric.util.MaterialUtil;
import org.sandboxpowered.sandbox.fabric.util.PropertyUtil;
import org.sandboxpowered.sandbox.fabric.util.ReflectionHelper;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.sandboxpowered.sandbox.fabric.util.wrapper.RenderUtilImpl;

import java.security.Policy;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
        Set<String> supportedMods = Sets.newHashSet("minecraft", "sandbox", "sandboxapi", "fabricloader");
        Sandbox.unsupportedModsLoaded = FabricLoader.getInstance().getAllMods().stream()
                .map(ModContainer::getMetadata)
                .map(ModMetadata::getId)
                .anyMatch(((Predicate<String>) supportedMods::contains).negate());
        Policy.setPolicy(new AddonSecurityPolicy());

        Registry.REGISTRIES.add(new Identifier("sandbox", "container"), SandboxRegistries.CONTAINER_FACTORIES);

        ((SandboxInternal.Registry) Registry.BLOCK).set(new BasicRegistry<>(Registry.BLOCK, Block.class, WrappingUtil::convert, WrappingUtil::convert));
        ((SandboxInternal.Registry) Registry.ITEM).set(new BasicRegistry<>(Registry.ITEM, Item.class, WrappingUtil::convert, WrappingUtil::convert));
        ((SandboxInternal.Registry) Registry.ENCHANTMENT).set(new BasicRegistry<>((SimpleRegistry<net.minecraft.enchantment.Enchantment>) Registry.ENCHANTMENT, Enchantment.class, WrappingUtil::convert, b -> (Enchantment) b));
        ((SandboxInternal.Registry) Registry.FLUID).set(new BasicRegistry<>(Registry.FLUID, Fluid.class, WrappingUtil::convert, WrappingUtil::convert));
        ((SandboxInternal.Registry) Registry.ENTITY_TYPE).set(new BasicRegistry<>(Registry.ENTITY_TYPE, Entity.Type.class, WrappingUtil::convert, WrappingUtil::convert));
        ((SandboxInternal.Registry) Registry.BLOCK_ENTITY).set(new BasicRegistry((SimpleRegistry) Registry.BLOCK_ENTITY, BlockEntity.Type.class, (Function<BlockEntity.Type, BlockEntityType>) WrappingUtil::convert, (Function<BlockEntityType, BlockEntity.Type>) WrappingUtil::convert, true)); // DONT TOUCH THIS FOR HEAVENS SAKE PLEASE GOD NO
        ((SandboxInternal.Registry) SandboxRegistries.CONTAINER_FACTORIES).set(new BasicRegistry<>(SandboxRegistries.CONTAINER_FACTORIES, ContainerFactory.class, a -> a, a -> a));
    }

    public static void shutdownGlobal() {
    }
}