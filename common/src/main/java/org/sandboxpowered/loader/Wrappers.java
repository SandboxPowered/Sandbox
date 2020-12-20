package org.sandboxpowered.loader;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.enchantment.Enchantment;
import org.sandboxpowered.api.entity.LivingEntity;
import org.sandboxpowered.api.fluid.Fluid;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.registry.Registry;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.util.text.Text;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.api.world.WorldReader;
import org.sandboxpowered.loader.wrapper.WrappedBlock;
import org.sandboxpowered.loader.wrapper.WrappedItem;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Wrappers {
    public static final Wrapper<Item, net.minecraft.world.item.Item> ITEM = new Wrapper<>(
            Item.class, net.minecraft.world.item.Item.class,
            WrappedItem::convertSandboxItem,
            WrappedItem::convertVanillaItem
    );
    public static final Wrapper<Text, Component> TEXT = new Wrapper<>(
            Text.class, Component.class
    );
    public static final Wrapper<World, Level> WORLD = new Wrapper<>(
            World.class, Level.class
    );
    public static final Wrapper<WorldReader, BlockGetter> WORLD_READER = new Wrapper<>(
            WorldReader.class, BlockGetter.class
    );
    public static final Wrapper<BlockEntity, net.minecraft.world.level.block.entity.BlockEntity> BLOCK_ENTITY = new Wrapper<>(
            BlockEntity.class, net.minecraft.world.level.block.entity.BlockEntity.class
    );
    public static final Wrapper<org.sandboxpowered.api.state.BlockState, BlockState> BLOCKSTATE = new Wrapper<>(
            org.sandboxpowered.api.state.BlockState.class, BlockState.class
    );
    public static final Wrapper<Position, BlockPos> POSITION = new Wrapper<>(
            Position.class, BlockPos.class
    );
    public static final Wrapper<Registry, net.minecraft.core.Registry> REGISTRY = new Wrapper<>(
            Registry.class, net.minecraft.core.Registry.class,
            s -> {
                if(s instanceof CacheableRegistry.Wrapped)
                    return ((CacheableRegistry.Wrapped<?, ?>) s).toVanilla();
                return null;
            },
            v -> {
                if(v instanceof CacheableRegistry)
                    return ((CacheableRegistry<?, ?>) v).getSandboxRegistry();
                return null;
            }
    );
    public static Wrapper<Block, net.minecraft.world.level.block.Block> BLOCK = new Wrapper<>(
            Block.class, net.minecraft.world.level.block.Block.class,
            WrappedBlock::convertSandboxBlock,
            WrappedBlock::convertVanillaBlock
    );
    public static Wrapper<Fluid, net.minecraft.world.level.material.Fluid> FLUID = new Wrapper<>(
            Fluid.class, net.minecraft.world.level.material.Fluid.class,
            fluid -> null,
            fluid -> null
    );
    public static Wrapper<Enchantment, net.minecraft.world.item.enchantment.Enchantment> ENCHANTMENT = new Wrapper<>(
            Enchantment.class, net.minecraft.world.item.enchantment.Enchantment.class,
            enchant -> null,
            enchant -> null
    );
    public static Wrapper<LivingEntity, net.minecraft.world.entity.LivingEntity> LIVING_ENTITY = new Wrapper<>(
            LivingEntity.class, net.minecraft.world.entity.LivingEntity.class
    );

    public static Wrapper<Identity, ResourceLocation> IDENTITY = new Wrapper<>(
            Identity.class, ResourceLocation.class
    );

    public static Wrapper<ItemStack, net.minecraft.world.item.ItemStack> ITEMSTACK = new Wrapper<>(
            ItemStack.class, net.minecraft.world.item.ItemStack.class
    );

    private static <X, Y> Function<X, Y> cast() {
        return i -> (Y) i;
    }

    public static class Wrapper<S, V> {
        private final Class<S> sandboxType;
        private final Class<V> vanillaType;
        private final Function<S, V> sandboxToVanilla;
        private final Function<V, S> vanillaToSandbox;
        private final Function<Collection<S>, Collection<V>> sandboxToVanillaCollection;
        private final Function<Collection<V>, Collection<S>> vanillaToSandboxCollection;

        public Wrapper(Class<S> sandboxType, Class<V> vanillaType, Function<S, V> sandboxToVanilla, Function<V, S> vanillaToSandbox) {
            this(sandboxType, vanillaType, sandboxToVanilla, vanillaToSandbox, c -> c.stream().map(sandboxToVanilla).collect(Collectors.toSet()), c -> c.stream().map(vanillaToSandbox).collect(Collectors.toList()));
        }

        public Wrapper(Class<S> sandboxType, Class<V> vanillaType) {
            this(sandboxType, vanillaType, cast(), cast(), cast(), cast());
        }

        public Wrapper(Class<S> sandboxType, Class<V> vanillaType, Function<S, V> sandboxToVanilla, Function<V, S> vanillaToSandbox, Function<Collection<S>, Collection<V>> sandboxToVanillaCollection, Function<Collection<V>, Collection<S>> vanillaToSandboxCollection) {
            this.sandboxType = sandboxType;
            this.vanillaType = vanillaType;
            this.sandboxToVanilla = sandboxToVanilla;
            this.vanillaToSandbox = vanillaToSandbox;
            this.sandboxToVanillaCollection = sandboxToVanillaCollection;
            this.vanillaToSandboxCollection = vanillaToSandboxCollection;
        }

        public V toVanilla(S sandbox) {
            return sandboxToVanilla.apply(sandbox);
        }

        public S toSandbox(V vanilla) {
            return vanillaToSandbox.apply(vanilla);
        }

        public Class<S> getSandboxType() {
            return sandboxType;
        }

        public Class<V> getVanillaType() {
            return vanillaType;
        }

        public Collection<S> toSandboxList(Collection<V> collection) {
            return vanillaToSandboxCollection.apply(collection);
        }

        public Collection<V> toVanillaList(Collection<S> collection) {
            return sandboxToVanillaCollection.apply(collection);
        }
    }
}