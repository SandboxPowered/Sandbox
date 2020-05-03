package org.sandboxpowered.sandbox.fabric.util;

import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.enchantment.Enchantment;
import org.sandboxpowered.api.entity.Entity;
import org.sandboxpowered.api.fluid.BaseFluid;
import org.sandboxpowered.api.fluid.Fluid;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.*;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.util.nbt.ReadableCompoundTag;
import org.sandboxpowered.api.world.BlockFlag;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.api.world.WorldReader;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.wrapper.*;

import java.util.function.Function;

public class WrappingUtil {

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
            if (((SandboxInternal.WrappedInjection) block).getInjectionWrapped() == null) {
                ((SandboxInternal.WrappedInjection) block).setInjectionWrapped(BlockWrapper.create(block));
            }
            return (net.minecraft.block.Block) ((SandboxInternal.WrappedInjection) block).getInjectionWrapped();
        }
        throw new RuntimeException("Unacceptable class " + block.getClass());
    }

    private static net.minecraft.item.Item getWrapped(Item item) {
        if (item instanceof SandboxInternal.WrappedInjection) {
            if (((SandboxInternal.WrappedInjection) item).getInjectionWrapped() == null) {
                ((SandboxInternal.WrappedInjection) item).setInjectionWrapped(ItemWrapper.create(item));
            }
            return (net.minecraft.item.Item) ((SandboxInternal.WrappedInjection) item).getInjectionWrapped();
        }
        throw new RuntimeException("Unacceptable class " + item.getClass());
    }

    private static net.minecraft.enchantment.Enchantment getWrapped(Enchantment enchantment) {
        if (enchantment instanceof SandboxInternal.WrappedInjection) {
            if (((SandboxInternal.WrappedInjection) enchantment).getInjectionWrapped() == null) {
                ((SandboxInternal.WrappedInjection) enchantment).setInjectionWrapped(new EnchantmentWrapper(enchantment));
            }
            return (net.minecraft.enchantment.Enchantment) ((SandboxInternal.WrappedInjection) enchantment).getInjectionWrapped();
        }
        throw new RuntimeException("Unacceptable class " + enchantment.getClass());
    }


    public static net.minecraft.enchantment.Enchantment convert(Enchantment enchant) {
        return castOrWrap(enchant, net.minecraft.enchantment.Enchantment.class, WrappingUtil::getWrapped);
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
        if (item instanceof SandboxInternal.ItemWrapper) {
            return ((SandboxInternal.ItemWrapper) item).getItem();
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

    public static net.minecraft.block.Block.Settings convert(Block.Settings settings) {
        return castOrWrap(settings, net.minecraft.block.Block.Settings.class, prop -> {
            net.minecraft.block.Block.Settings vs = net.minecraft.block.Block.Settings.of(convert(settings.getMaterial()));
            SandboxInternal.MaterialInternal ms = (SandboxInternal.MaterialInternal) vs;
            vs.velocityMultiplier(settings.getVelocity());
            vs.jumpVelocityMultiplier(settings.getJumpVelocity());
            vs.slipperiness(settings.getSlipperiness());
            vs.strength(settings.getHardness(), settings.getResistance());
            ms.sbxsetlevel(settings.getLuminance());
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
                default:
                    continue;
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
        return Direction.values()[direction.ordinal()];
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

    public static BlockEntityType convert(BlockEntity.Type type) {
        return cast(type, BlockEntityType.class);
    }

    public static EntityType convert(Entity.Type type) {
        return cast(type, EntityType.class);
    }

    public static Entity.Type convert(EntityType type) {
        return cast(type, Entity.Type.class);
    }

    public static BlockEntity.Type convert(BlockEntityType type) {
        return cast(type, BlockEntity.Type.class);
    }

    public static Text convert(org.sandboxpowered.api.util.text.Text type) {
        return cast(type, Text.class);
    }

    public static Block convert(net.minecraft.block.Block block) {
        if (block instanceof SandboxInternal.BlockWrapper)
            return ((SandboxInternal.BlockWrapper) block).getBlock();
        return (Block) block;
    }

    public static Entity convert(net.minecraft.entity.Entity entity_1) {
        return (Entity) entity_1;
    }

    public static net.minecraft.entity.Entity convert(Entity entity_1) {
        if (entity_1 == null)
            return null;
        return (net.minecraft.entity.Entity) entity_1;
    }

    public static Property convert(org.sandboxpowered.api.state.Property property) {
        //TODO: Wrapper
        return (Property) property;
    }

    public static Fluid convert(net.minecraft.fluid.Fluid fluid_1) {
        if (fluid_1 instanceof FluidWrapper)
            return ((FluidWrapper) fluid_1).fluid;
        return (Fluid) fluid_1;
    }

    private static net.minecraft.fluid.Fluid getWrapped(Fluid fluid) {
        if (fluid instanceof BaseFluid && fluid instanceof SandboxInternal.WrappedInjection) {
            if (((SandboxInternal.WrappedInjection) fluid).getInjectionWrapped() == null) {
                ((SandboxInternal.WrappedInjection) fluid).setInjectionWrapped(FluidWrapper.create((BaseFluid) fluid));
            }
            return (net.minecraft.fluid.Fluid) ((SandboxInternal.WrappedInjection) fluid).getInjectionWrapped();
        }
        throw new RuntimeException("Unacceptable class " + fluid.getClass());
    }

    public static net.minecraft.fluid.Fluid convert(Fluid fluid_1) {
        return castOrWrap(fluid_1, net.minecraft.fluid.Fluid.class, WrappingUtil::getWrapped);
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

    public static Vector3d convertToVector(org.sandboxpowered.api.util.math.Vec3d vector3dc) {
        return null; //TODO
    }
    public static Vec3d convertToVec(org.sandboxpowered.api.util.math.Vec3d vector3dc) {
        return null; //TODO
    }
}