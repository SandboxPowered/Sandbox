package com.hrznstudio.sandbox.util;

import com.hrznstudio.sandbox.api.SandboxInternal;
import com.hrznstudio.sandbox.api.block.IBlock;
import com.hrznstudio.sandbox.api.block.entity.IBlockEntity;
import com.hrznstudio.sandbox.api.client.screen.IScreen;
import com.hrznstudio.sandbox.api.enchant.IEnchantment;
import com.hrznstudio.sandbox.api.entity.IEntity;
import com.hrznstudio.sandbox.api.fluid.IFluid;
import com.hrznstudio.sandbox.api.item.IItem;
import com.hrznstudio.sandbox.api.item.ItemStack;
import com.hrznstudio.sandbox.api.state.BlockState;
import com.hrznstudio.sandbox.api.util.Direction;
import com.hrznstudio.sandbox.api.util.Identity;
import com.hrznstudio.sandbox.api.util.Mirror;
import com.hrznstudio.sandbox.api.util.Rotation;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.BlockFlag;
import com.hrznstudio.sandbox.api.world.World;
import com.hrznstudio.sandbox.api.world.WorldReader;
import com.hrznstudio.sandbox.util.wrapper.*;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.function.Function;

public class WrappingUtil {

    public static BlockPos convert(Position position) {
        return castOrWrap(position, BlockPos.class, p -> new BlockPosWrapper(position));
    }

    public static net.minecraft.block.BlockState convert(BlockState state) {
        return castOrWrap(state, net.minecraft.block.BlockState.class, s -> null);
    }

    public static Block convert(IBlock block) {
        return castOrWrap(block, Block.class, WrappingUtil::getWrapped);
    }

    private static Block getWrapped(IBlock block) {
        if (block instanceof SandboxInternal.WrappedInjection) {
            if (((SandboxInternal.WrappedInjection) block).getInjectionWrapped() == null) {
                ((SandboxInternal.WrappedInjection) block).setInjectionWrapped(BlockWrapper.create(block));
            }
            return (Block) ((SandboxInternal.WrappedInjection) block).getInjectionWrapped();
        }
        throw new RuntimeException("Unacceptable class " + block.getClass());
    }

    private static Item getWrapped(IItem item) {
        if (item instanceof SandboxInternal.WrappedInjection) {
            if (((SandboxInternal.WrappedInjection) item).getInjectionWrapped() == null) {
                ((SandboxInternal.WrappedInjection) item).setInjectionWrapped(ItemWrapper.create(item));
            }
            return (Item) ((SandboxInternal.WrappedInjection) item).getInjectionWrapped();
        }
        throw new RuntimeException("Unacceptable class " + item.getClass());
    }

    private static net.minecraft.enchantment.Enchantment getWrapped(IEnchantment enchantment) {
        if (enchantment instanceof SandboxInternal.WrappedInjection) {
            if (((SandboxInternal.WrappedInjection) enchantment).getInjectionWrapped() == null) {
                ((SandboxInternal.WrappedInjection) enchantment).setInjectionWrapped(new EnchantmentWrapper(enchantment));
            }
            return (Enchantment) ((SandboxInternal.WrappedInjection) enchantment).getInjectionWrapped();
        }
        throw new RuntimeException("Unacceptable class " + enchantment.getClass());
    }


    public static net.minecraft.enchantment.Enchantment convert(IEnchantment enchant) {
        return castOrWrap(enchant, net.minecraft.enchantment.Enchantment.class, WrappingUtil::getWrapped);
    }

    public static Block[] convert(IBlock[] block) {
        Block[] arr = new Block[block.length];
        for (int i = 0; i < block.length; i++) {
            arr[i] = convert(block[i]);
        }
        return arr;
    }

    public static net.minecraft.item.Item convert(IItem item) {
        return castOrWrap(item, net.minecraft.item.Item.class, WrappingUtil::getWrapped);
    }

    public static IItem convert(net.minecraft.item.Item item) {
        if (item instanceof SandboxInternal.ItemWrapper) {
            return ((SandboxInternal.ItemWrapper) item).getItem();
        }
        return (IItem) item;
    }

    public static PistonBehavior convert(com.hrznstudio.sandbox.api.block.Material.PistonInteraction interaction) {
        return PistonBehavior.values()[interaction.ordinal()];
    }

    public static com.hrznstudio.sandbox.api.block.Material.PistonInteraction convert(PistonBehavior behavior) {
        return com.hrznstudio.sandbox.api.block.Material.PistonInteraction.values()[behavior.ordinal()];
    }

    public static <A, B> B cast(A a, Class<B> bClass) {
        return bClass.cast(a);
    }

    private static <A, B> B castOrWrap(A a, Class<B> bClass, Function<A, B> wrapper) {
        if (bClass.isInstance(a))
            return bClass.cast(a);
        return wrapper.apply(a);
    }

    public static Block.Settings convert(IBlock.Settings settings) {
        return castOrWrap(settings, Block.Settings.class, prop -> Block.Settings.of(convert(settings.getMaterial())));
    }

    private static Material convert(com.hrznstudio.sandbox.api.block.Material material) {
        return castOrWrap(material, Material.class, m -> null);
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

    public static net.minecraft.block.entity.BlockEntity convert(IBlockEntity entity) {
        return castOrWrap(entity, net.minecraft.block.entity.BlockEntity.class, read -> BlockEntityWrapper.create(entity));
    }

    public static net.minecraft.item.ItemStack convert(ItemStack itemStack) {
        return cast(itemStack, net.minecraft.item.ItemStack.class);
    }

    public static BlockEntityType convert(IBlockEntity.Type type) {
        return cast(type, BlockEntityType.class);
    }

    public static IBlockEntity.Type convert(BlockEntityType type) {
        return cast(type, IBlockEntity.Type.class);
    }

    public static Text convert(com.hrznstudio.sandbox.api.util.text.Text type) {
        return cast(type, Text.class);
    }

    public static IBlock convert(Block block) {
        if (block instanceof SandboxInternal.BlockWrapper)
            return ((SandboxInternal.BlockWrapper) block).getBlock();
        return (IBlock) block;
    }

    public static IEntity convert(Entity entity_1) {
        return (IEntity) entity_1;
    }

    public static Screen getWrapped(IScreen screen) {
        if (screen instanceof SandboxInternal.WrappedInjection) {
            if (((SandboxInternal.WrappedInjection) screen).getInjectionWrapped() == null) {
                ((SandboxInternal.WrappedInjection) screen).setInjectionWrapped(ScreenWrapper.create((com.hrznstudio.sandbox.api.client.screen.Screen) screen));
            }
            return (Screen) ((SandboxInternal.WrappedInjection) screen).getInjectionWrapped();
        }
        throw new RuntimeException("Unacceptable class " + screen.getClass());
    }

    public static Screen convert(IScreen screen) {
        return castOrWrap(screen, Screen.class, WrappingUtil::getWrapped);
    }

    public static Property convert(com.hrznstudio.sandbox.api.state.Property property) {
        //TODO: Wrapper
        return (Property) property;
    }

    public static IScreen convert(Screen screen) {
        if (screen instanceof ScreenWrapper)
            return ((ScreenWrapper) screen).screen;
        return cast(screen, IScreen.class);
    }

    public static IFluid convert(Fluid fluid_1) {
        if (fluid_1 instanceof FluidWrapper)
            return ((FluidWrapper) fluid_1).fluid;
        return (IFluid) fluid_1;
    }

    private static Fluid getWrapped(IFluid fluid) {
        if (fluid instanceof com.hrznstudio.sandbox.api.fluid.Fluid && fluid instanceof SandboxInternal.WrappedInjection) {
            if (((SandboxInternal.WrappedInjection) fluid).getInjectionWrapped() == null) {
                ((SandboxInternal.WrappedInjection) fluid).setInjectionWrapped(FluidWrapper.create((com.hrznstudio.sandbox.api.fluid.Fluid) fluid));
            }
            return (Fluid) ((SandboxInternal.WrappedInjection) fluid).getInjectionWrapped();
        }
        throw new RuntimeException("Unacceptable class " + fluid.getClass());
    }

    public static Fluid convert(IFluid fluid_1) {
        return castOrWrap(fluid_1, Fluid.class, WrappingUtil::getWrapped);
    }

    public static Item.Settings convert(IItem.Settings settings) {
        return new Item.Settings().maxCount(settings.getStackSize()).maxDamage(settings.getMaxDamage()).recipeRemainder(settings.getRecipeRemainder()==null?null:convert(settings.getRecipeRemainder()));
    }
}