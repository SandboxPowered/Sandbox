package org.sandboxpowered.loader;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.block.Material;
import org.sandboxpowered.api.enchantment.Enchantment;
import org.sandboxpowered.api.fluid.Fluid;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.item.attribute.Attribute;
import org.sandboxpowered.api.item.tool.ToolMaterial;
import org.sandboxpowered.api.registry.Registry;
import org.sandboxpowered.api.util.EquipmentSlot;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.util.InteractionResult;
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
    public static final Wrapper<org.sandboxpowered.api.state.BlockState, BlockState> BLOCKSTATE = new Wrapper<>(
            org.sandboxpowered.api.state.BlockState.class, BlockState.class
    );
    public static final Wrapper<Position, BlockPos> POSITION = new Wrapper<>(
            Position.class, BlockPos.class
    );
    public static final Wrapper<Registry, net.minecraft.core.Registry> REGISTRY = new Wrapper<>(
            Registry.class, net.minecraft.core.Registry.class,
            s -> {
                if (s instanceof CacheableRegistry.Wrapped)
                    return ((CacheableRegistry.Wrapped<?, ?>) s).toVanilla();
                return null;
            },
            v -> {
                if (v instanceof CacheableRegistry)
                    return ((CacheableRegistry<?, ?>) v).getSandboxRegistry();
                return null;
            }
    );
    public static final Wrapper<InteractionResult, net.minecraft.world.InteractionResult> INTERACTION_RESULT = new Wrapper<>(
            InteractionResult.class, net.minecraft.world.InteractionResult.class,
            result -> {
                switch (result) {
                    case CONSUME:
                        return net.minecraft.world.InteractionResult.CONSUME;
                    case SUCCESS:
                        return net.minecraft.world.InteractionResult.SUCCESS;
                    case FAILURE:
                        return net.minecraft.world.InteractionResult.FAIL;
                    default:
                        return net.minecraft.world.InteractionResult.PASS;
                }
            },
            result -> {
                switch (result) {
                    case CONSUME:
                        return InteractionResult.CONSUME;
                    case SUCCESS:
                        return InteractionResult.SUCCESS;
                    case FAIL:
                        return InteractionResult.FAILURE;
                    default:
                        return InteractionResult.IGNORE;
                }
            }
    );
    public static final Wrapper<Material, net.minecraft.world.level.material.Material> MATERIAL = new Wrapper<>(
            Material.class, net.minecraft.world.level.material.Material.class
    );
    public static final Wrapper<Position.Mutable, BlockPos.MutableBlockPos> MUTABLE_POSITION = new Wrapper<>(
            Position.Mutable.class, BlockPos.MutableBlockPos.class
    );
    public static final Wrapper<Attribute, net.minecraft.world.entity.ai.attributes.Attribute> ATTRIBUTE = new Wrapper<>(
            Attribute.class, net.minecraft.world.entity.ai.attributes.Attribute.class
    );
    public static final Wrapper<ToolMaterial, Tier> TOOL_MATERIAL = new Wrapper<>(
            ToolMaterial.class, Tier.class
    );
    public static final Wrapper<Block, net.minecraft.world.level.block.Block> BLOCK = new Wrapper<>(
            Block.class, net.minecraft.world.level.block.Block.class,
            WrappedBlock::convertSandboxBlock,
            WrappedBlock::convertVanillaBlock
    );
    public static final Wrapper<Fluid, net.minecraft.world.level.material.Fluid> FLUID = new Wrapper<>(
            Fluid.class, net.minecraft.world.level.material.Fluid.class,
            fluid -> null,
            fluid -> null
    );
    public static final Wrapper<Enchantment, net.minecraft.world.item.enchantment.Enchantment> ENCHANTMENT = new Wrapper<>(
            Enchantment.class, net.minecraft.world.item.enchantment.Enchantment.class,
            enchant -> null,
            enchant -> null
    );

    public static final Wrapper<Identity, ResourceLocation> IDENTITY = new Wrapper<>(
            Identity.class, ResourceLocation.class
    );

    public static final Wrapper<ItemStack, net.minecraft.world.item.ItemStack> ITEMSTACK = new Wrapper<>(
            ItemStack.class, net.minecraft.world.item.ItemStack.class
    );
    public static final Wrapper<EquipmentSlot, net.minecraft.world.entity.EquipmentSlot> EQUIPMENT_SLOT = new Wrapper<>(
            EquipmentSlot.class, net.minecraft.world.entity.EquipmentSlot.class,
            slot -> {
                switch (slot) {
                    case MAIN_HAND:
                        return net.minecraft.world.entity.EquipmentSlot.MAINHAND;
                    case FEET:
                        return net.minecraft.world.entity.EquipmentSlot.FEET;
                    case LEGS:
                        return net.minecraft.world.entity.EquipmentSlot.LEGS;
                    case CHEST:
                        return net.minecraft.world.entity.EquipmentSlot.CHEST;
                    case HEAD:
                        return net.minecraft.world.entity.EquipmentSlot.HEAD;
                    case OFF_HAND:
                    default:
                        return net.minecraft.world.entity.EquipmentSlot.OFFHAND;
                }
            },
            slot -> {
                switch (slot) {
                    case MAINHAND:
                        return EquipmentSlot.MAIN_HAND;
                    case FEET:
                        return EquipmentSlot.FEET;
                    case LEGS:
                        return EquipmentSlot.LEGS;
                    case CHEST:
                        return EquipmentSlot.CHEST;
                    case HEAD:
                        return EquipmentSlot.HEAD;
                    case OFFHAND:
                    default:
                        return EquipmentSlot.OFF_HAND;
                }
            }
    );
    public static final Wrapper<Attribute.Modifier, AttributeModifier> ATTRIBUTE_MODIFIER = new Wrapper<>(
            Attribute.Modifier.class, AttributeModifier.class
    );
    public static final Wrapper<Attribute.Operation, AttributeModifier.Operation> ATTRIBUTE_OPERATION = new Wrapper<>(
            Attribute.Operation.class, AttributeModifier.Operation.class,
            operation -> {
                switch (operation) {
                    case MULTIPLY_BASE:
                        return AttributeModifier.Operation.MULTIPLY_BASE;
                    case MULTIPLY_TOTAL:
                        return AttributeModifier.Operation.MULTIPLY_TOTAL;
                    case ADDITION:
                    default:
                        return AttributeModifier.Operation.ADDITION;
                }
            },
            operation -> {
                switch (operation) {
                    case MULTIPLY_BASE:
                        return Attribute.Operation.MULTIPLY_BASE;
                    case MULTIPLY_TOTAL:
                        return Attribute.Operation.MULTIPLY_TOTAL;
                    case ADDITION:
                    default:
                        return Attribute.Operation.ADDITION;
                }
            }
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