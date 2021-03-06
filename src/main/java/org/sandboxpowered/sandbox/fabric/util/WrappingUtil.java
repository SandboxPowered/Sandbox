package org.sandboxpowered.sandbox.fabric.util;

import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.client.GraphicsMode;
import org.sandboxpowered.api.client.rendering.VertexConsumer;
import org.sandboxpowered.api.enchantment.Enchantment;
import org.sandboxpowered.api.entity.Entity;
import org.sandboxpowered.api.entity.LivingEntity;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.sandboxpowered.api.fluid.BaseFluid;
import org.sandboxpowered.api.fluid.Fluid;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.shape.Box;
import org.sandboxpowered.api.shape.Shape;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.*;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.util.nbt.ReadableCompoundTag;
import org.sandboxpowered.api.world.BlockFlag;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.api.world.WorldReader;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.exception.WrappingException;
import org.sandboxpowered.sandbox.fabric.util.wrapper.*;

import java.util.function.Function;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public class WrappingUtil {

    private WrappingUtil() {
    }

    public static BlockPos convert(Position position) {
        return castOrWrap(position, BlockPos.class, p -> new BlockPosWrapper(position));
    }

    public static Position convert(BlockPos position) {
        return cast(position, Position.class);
    }

    public static net.minecraft.block.BlockState convert(BlockState state) {
        return castOrWrap(state, net.minecraft.block.BlockState.class, s -> null);
    }

    public static net.minecraft.block.Block convert(Block block) {
        return castOrWrap(block, net.minecraft.block.Block.class, WrappingUtil::getWrapped);
    }

    private static net.minecraft.block.Block getWrapped(Block block) {
        if (block instanceof SandboxInternal.WrappedInjection) {
            SandboxInternal.WrappedInjection<SandboxInternal.IBlockWrapper> wrappedInjection = (SandboxInternal.WrappedInjection<SandboxInternal.IBlockWrapper>) block;
            if (wrappedInjection.getInjectionWrapped() == null) {
                wrappedInjection.setInjectionWrapped(BlockWrapper.create(block));
            }
            return (net.minecraft.block.Block) wrappedInjection.getInjectionWrapped();
        }
        throw new WrappingException(block.getClass());
    }

    private static net.minecraft.item.Item getWrapped(Item item) {
        if (item instanceof SandboxInternal.WrappedInjection) {
            SandboxInternal.WrappedInjection<SandboxInternal.IItemWrapper> wrappedInjection = (SandboxInternal.WrappedInjection<SandboxInternal.IItemWrapper>) item;
            if (wrappedInjection.getInjectionWrapped() == null) {
                wrappedInjection.setInjectionWrapped(ItemWrapper.create(item));
            }
            return (net.minecraft.item.Item) wrappedInjection.getInjectionWrapped();
        }
        throw new WrappingException(item.getClass());
    }

    private static net.minecraft.enchantment.Enchantment getWrapped(Enchantment enchantment) {
        if (enchantment instanceof SandboxInternal.WrappedInjection) {
            SandboxInternal.WrappedInjection<EnchantmentWrapper> wrappedInjection = (SandboxInternal.WrappedInjection<EnchantmentWrapper>) enchantment;
            if (wrappedInjection.getInjectionWrapped() == null) {
                wrappedInjection.setInjectionWrapped(new EnchantmentWrapper(enchantment));
            }
            return wrappedInjection.getInjectionWrapped();
        }
        throw new WrappingException(enchantment.getClass());
    }

    private static net.minecraft.fluid.Fluid getWrapped(Fluid fluid) {
        if (fluid instanceof BaseFluid && fluid instanceof SandboxInternal.WrappedInjection) {
            SandboxInternal.WrappedInjection<FluidWrapper> wrappedInjection = (SandboxInternal.WrappedInjection<FluidWrapper>) fluid;
            if (wrappedInjection.getInjectionWrapped() == null) {
                wrappedInjection.setInjectionWrapped(FluidWrapper.create((BaseFluid) fluid));
            }
            return wrappedInjection.getInjectionWrapped();
        }
        throw new WrappingException(fluid.getClass());
    }

    public static net.minecraft.enchantment.Enchantment convert(Enchantment enchant) {
        return castOrWrap(enchant, net.minecraft.enchantment.Enchantment.class, WrappingUtil::getWrapped);
    }

    //Don't remove, used by the LambdaMetaFactory in the registries
    public static Enchantment convert(net.minecraft.enchantment.Enchantment enchant) {
        return (Enchantment) enchant;
    }

    public static net.minecraft.block.Block[] convert(Block[] block) {
        net.minecraft.block.Block[] arr = new net.minecraft.block.Block[block.length];
        for (int i = 0; i < block.length; i++) {
            arr[i] = convert(block[i]);
        }
        return arr;
    }

    public static net.minecraft.item.Item convert(Item item) {
        return castOrWrap(item, net.minecraft.item.Item.class, WrappingUtil::getWrapped);
    }

    public static Item convert(net.minecraft.item.Item item) {
        if (item instanceof SandboxInternal.IItemWrapper) {
            return ((SandboxInternal.IItemWrapper) item).getItem();
        }
        return (Item) item;
    }

    public static PistonBehavior convert(org.sandboxpowered.api.block.Material.PistonInteraction interaction) {
        return PistonBehavior.values()[interaction.ordinal()];
    }

    public static org.sandboxpowered.api.block.Material.PistonInteraction convert(PistonBehavior behavior) {
        return org.sandboxpowered.api.block.Material.PistonInteraction.values()[behavior.ordinal()];
    }

    public static <A, B> B cast(A a, Class<B> bClass) {
        return bClass.cast(a);
    }

    private static <A, B> B castOrWrap(A a, Class<B> bClass, Function<A, B> wrapper) {
        if (bClass.isInstance(a))
            return bClass.cast(a);
        return wrapper.apply(a);
    }

    public static Settings convert(Block.Settings settings) {
        return castOrWrap(settings, Settings.class, prop -> {
            Settings vs = Settings.of(convert(settings.getMaterial()));
            SandboxInternal.MaterialInternal ms = (SandboxInternal.MaterialInternal) vs;
            vs.velocityMultiplier(settings.getVelocity());
            vs.jumpVelocityMultiplier(settings.getJumpVelocity());
            vs.slipperiness(settings.getSlipperiness());
            vs.strength(settings.getHardness(), settings.getResistance());
            ms.setLevel(settings.getLuminance());
            return vs;
        });
    }

    public static Material convert(org.sandboxpowered.api.block.Material material) {
        return castOrWrap(material, Material.class, m -> null);
    }

    public static org.sandboxpowered.api.block.Material convert(Material material) {
        return castOrWrap(material, org.sandboxpowered.api.block.Material.class, m -> null);
    }

    public static int convert(BlockFlag[] flags) {
        int r = 0b00000;
        for (BlockFlag flag : flags) {
            switch (flag) {
                case NOTIFY_NEIGHBORS:
                    r |= 0b00001;
                    continue;
                case SEND_TO_CLIENT:
                    r |= 0b00010;
                    continue;
                case NO_RERENDER:
                    r |= 0b00100;
                    continue;
                case RERENDER_MAIN_THREAD:
                    r |= 0b01000;
                    continue;
                case NO_OBSERVER:
                    r |= 0b10000;
                    continue;
            }
        }
        return r;
    }

    public static Identifier convert(Identity identity) {
        return castOrWrap(identity, Identifier.class, id -> new Identifier(id.getNamespace(), id.getPath()));
    }

    public static net.minecraft.util.math.Direction convert(Direction direction) {
        return net.minecraft.util.math.Direction.byId(direction.ordinal());
    }

    public static Direction convert(net.minecraft.util.math.Direction direction) {
        return Direction.byId(direction.ordinal());
    }

    public static Mirror convert(BlockMirror mirror) {
        return Mirror.values()[mirror.ordinal()];
    }

    public static BlockMirror convert(Mirror mirror) {
        return BlockMirror.values()[mirror.ordinal()];
    }

    public static Rotation convert(BlockRotation rotation) {
        return Rotation.values()[rotation.ordinal()];
    }

    public static BlockRotation convert(Rotation rotation) {
        return BlockRotation.values()[rotation.ordinal()];
    }

    public static BlockView convert(WorldReader reader) {
        return castOrWrap(reader, BlockView.class, read -> null);
    }

    public static net.minecraft.world.World convert(World reader) {
        return castOrWrap(reader, net.minecraft.world.World.class, read -> null);
    }

    public static World convert(net.minecraft.world.World world) {
        return castOrWrap(world, World.class, read -> null);
    }

    public static net.minecraft.block.entity.BlockEntity convert(BlockEntity entity) {
        return castOrWrap(entity, net.minecraft.block.entity.BlockEntity.class, read -> BlockEntityWrapper.create(entity));
    }

    public static BlockEntity convert(net.minecraft.block.entity.BlockEntity entity) {
        if (entity instanceof BlockEntityWrapper)
            return ((BlockEntityWrapper) entity).getBlockEntity();
        return (BlockEntity) entity;
    }

    public static net.minecraft.item.ItemStack convert(ItemStack itemStack) {
        return cast(itemStack, net.minecraft.item.ItemStack.class);
    }

    public static ItemStack convert(net.minecraft.item.ItemStack itemStack) {
        return cast(itemStack, ItemStack.class);
    }

    public static EntityType<?> convert(Entity.Type type) {
        return cast(type, EntityType.class);
    }

    public static Entity.Type convert(EntityType<?> type) {
        return cast(type, Entity.Type.class);
    }

    public static Text convert(org.sandboxpowered.api.util.text.Text type) {
        return cast(type, Text.class);
    }

    public static Block convert(net.minecraft.block.Block block) {
        if (block instanceof SandboxInternal.IBlockWrapper)
            return ((SandboxInternal.IBlockWrapper) block).getBlock();
        return (Block) block;
    }

    public static Entity convert(net.minecraft.entity.Entity entity) {
        return (Entity) entity;
    }

    public static PlayerEntity convert(net.minecraft.entity.player.PlayerEntity player) {
        return (PlayerEntity) player;
    }

    public static net.minecraft.entity.player.PlayerEntity convert(PlayerEntity player) {
        return (net.minecraft.entity.player.PlayerEntity) player;
    }

    public static net.minecraft.entity.Entity convert(Entity entity) {
        return (net.minecraft.entity.Entity) entity;
    }

    public static <T extends Comparable<T>> Property<T> convert(org.sandboxpowered.api.state.Property<T> property) {
        if (property instanceof EnumPropertyWrapper) {
            return (Property<T>) ((EnumPropertyWrapper<?, ?>) property).getEnumProperty();
        }
        //TODO: Wrapper
        return (Property<T>) property;
    }

    public static Fluid convert(net.minecraft.fluid.Fluid fluid) {
        if (fluid instanceof FluidWrapper)
            return ((FluidWrapper) fluid).getFluid();
        return (Fluid) fluid;
    }

    public static net.minecraft.fluid.Fluid convert(Fluid fluid) {
        return castOrWrap(fluid, net.minecraft.fluid.Fluid.class, WrappingUtil::getWrapped);
    }

    public static net.minecraft.item.Item.Settings convert(Item.Settings settings) {
        return new net.minecraft.item.Item.Settings().maxCount(settings.getStackSize()).maxDamage(settings.getMaxDamage()).recipeRemainder(settings.getRecipeRemainder() == null ? null : convert(settings.getRecipeRemainder()));
    }

    public static ActionResult convert(InteractionResult result) {
        switch (result) {
            case SUCCESS:
                return ActionResult.SUCCESS;
            case CONSUME:
                return ActionResult.CONSUME;
            case FAILURE:
                return ActionResult.FAIL;
            default:
            case IGNORE:
                return ActionResult.PASS;
        }
    }

    public static InteractionResult convert(ActionResult result) {
        switch (result) {
            case SUCCESS:
                return InteractionResult.SUCCESS;
            case CONSUME:
                return InteractionResult.CONSUME;
            case FAIL:
                return InteractionResult.FAILURE;
            default:
            case PASS:
                return InteractionResult.IGNORE;
        }
    }

    public static CompoundTag convert(ReadableCompoundTag tag) {
        return (net.minecraft.nbt.CompoundTag) tag;
    }

    public static WorldReader convert(BlockView view) {
        return cast(view, WorldReader.class);
    }

    public static Vector3d convertToVector(org.sandboxpowered.api.util.math.Vec3d vec3) {
        return cast(vec3, Vector3d.class);
    }

    public static Vec3d convertToVec(org.sandboxpowered.api.util.math.Vec3d vec3) {
        return cast(vec3, Vec3d.class);
    }

    public static <T> RegistryKey<T> convertToRegistryKey(RegistryKey<Registry<T>> registryKey, Identity identity) {
        return RegistryKey.of(registryKey, convert(identity));
    }

    public static FluidState convert(org.sandboxpowered.api.state.FluidState state) {
        return (FluidState) (Object) state;
    }

    public static org.sandboxpowered.api.state.FluidState convert(FluidState state) {
        return (org.sandboxpowered.api.state.FluidState) (Object) state;
    }

    public static BlockState convert(net.minecraft.block.BlockState state) {
        return (BlockState) state;
    }

    public static net.minecraft.enchantment.Enchantment.Rarity convert(Enchantment.Rarity rarity) {
        switch (rarity) {
            case UNCOMMON:
                return net.minecraft.enchantment.Enchantment.Rarity.UNCOMMON;
            case RARE:
                return net.minecraft.enchantment.Enchantment.Rarity.RARE;
            case VERY_RARE:
                return net.minecraft.enchantment.Enchantment.Rarity.VERY_RARE;
            case COMMON:
            default:
                return net.minecraft.enchantment.Enchantment.Rarity.COMMON;
        }
    }

    public static GraphicsMode convert(net.minecraft.client.options.GraphicsMode graphicsMode) {
        switch (graphicsMode) {
            case FAST:
                return GraphicsMode.FAST;
            case FANCY:
                return GraphicsMode.FANCY;
            case FABULOUS:
                return GraphicsMode.FABULOUS;
        }
        return GraphicsMode.FAST;
    }

    public static org.sandboxpowered.api.util.math.Vec3d convert(Vec3d pos) {
        return (org.sandboxpowered.api.util.math.Vec3d) pos;
    }

    public static Direction.Axis convert(net.minecraft.util.math.Direction.Axis axis) {
        switch (axis) {
            case X:
                return Direction.Axis.X;
            case Y:
                return Direction.Axis.Y;
            case Z:
                return Direction.Axis.Z;
        }
        return Direction.Axis.X;
    }

    public static net.minecraft.util.math.Direction.Axis convert(Direction.Axis axis) {
        switch (axis) {
            case X:
                return net.minecraft.util.math.Direction.Axis.X;
            case Y:
                return net.minecraft.util.math.Direction.Axis.Y;
            case Z:
                return net.minecraft.util.math.Direction.Axis.Z;
        }
        return net.minecraft.util.math.Direction.Axis.X;
    }

    public static SlabType convert(net.minecraft.block.enums.SlabType slabType) {
        switch (slabType) {
            case TOP:
                return SlabType.TOP;
            case BOTTOM:
                return SlabType.BOTTOM;
            case DOUBLE:
                return SlabType.DOUBLE;
        }
        return SlabType.BOTTOM;
    }

    public static net.minecraft.block.enums.SlabType convert(SlabType slabType) {
        switch (slabType) {
            case TOP:
                return net.minecraft.block.enums.SlabType.TOP;
            case BOTTOM:
                return net.minecraft.block.enums.SlabType.BOTTOM;
            case DOUBLE:
                return net.minecraft.block.enums.SlabType.DOUBLE;
        }
        return net.minecraft.block.enums.SlabType.BOTTOM;
    }

    public static Shape convert(VoxelShape shape) {
        return (Shape) shape;
    }

    public static VoxelShape convert(Shape shape) {
        return (VoxelShape) shape;
    }

    public static Box convert(net.minecraft.util.math.Box boundingBox) {
        return (Box) boundingBox;
    }

    public static net.minecraft.util.math.Box convert(Box box) {
        return (net.minecraft.util.math.Box) box;
    }

    public static Hand convert(org.sandboxpowered.api.entity.player.Hand hand) {
        return hand == org.sandboxpowered.api.entity.player.Hand.MAIN_HAND ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }
    public static org.sandboxpowered.api.entity.player.Hand convert(Hand hand) {
        return hand == Hand.MAIN_HAND ? org.sandboxpowered.api.entity.player.Hand.MAIN_HAND : org.sandboxpowered.api.entity.player.Hand.OFF_HAND;
    }

    public static EquipmentSlot convert(LivingEntity.EquipmentSlot slot) {
        switch (slot) {
            case FEET:
                return EquipmentSlot.FEET;
            case HEAD:
                return EquipmentSlot.HEAD;
            case LEGS:
                return EquipmentSlot.LEGS;
            case CHEST:
                return EquipmentSlot.CHEST;
            case OFFHAND:
                return EquipmentSlot.OFFHAND;
            case MAINHAND:
                return EquipmentSlot.MAINHAND;
        }
        return null;
    }

    public static MatrixStack convert(org.sandboxpowered.api.util.math.MatrixStack stack) {
        return cast(stack, MatrixStack.class);
    }

    public static org.sandboxpowered.api.util.math.MatrixStack convert(MatrixStack stack) {
        return cast(stack, org.sandboxpowered.api.util.math.MatrixStack.class);
    }

    public static VertexConsumer.Provider convert(VertexConsumerProvider consumerProvider) {
        return null;
    }

    @Nullable
    public static net.minecraft.entity.LivingEntity convertToLivingOrNull(Entity entity) {
        if (entity instanceof net.minecraft.entity.LivingEntity)
            return (net.minecraft.entity.LivingEntity) entity;
        return null;
    }

    public static <V extends net.minecraft.block.entity.BlockEntity, S extends BlockEntity> BlockEntityType<V> convert(BlockEntity.Type<S> type) {
        return cast(type, BlockEntityType.class);
    }

    public static <V extends net.minecraft.block.entity.BlockEntity, S extends BlockEntity> BlockEntity.Type<S> convert(BlockEntityType<V> type) {
        return cast(type, BlockEntity.Type.class);
    }
}