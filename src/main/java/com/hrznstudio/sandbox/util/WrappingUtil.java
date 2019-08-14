package com.hrznstudio.sandbox.util;

import com.hrznstudio.sandbox.api.block.entity.BlockEntity;
import com.hrznstudio.sandbox.api.block.state.BlockState;
import com.hrznstudio.sandbox.api.item.Item;
import com.hrznstudio.sandbox.api.util.Direction;
import com.hrznstudio.sandbox.api.util.Identity;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.BlockFlag;
import com.hrznstudio.sandbox.api.world.WorldReader;
import com.hrznstudio.sandbox.util.wrapper.BlockEntityWrapper;
import com.hrznstudio.sandbox.util.wrapper.BlockPosWrapper;
import com.hrznstudio.sandbox.util.wrapper.BlockWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
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

    public static Block convert(com.hrznstudio.sandbox.api.block.Block block) {
        return castOrWrap(block, Block.class, s -> BlockWrapper.create(block));
    }

    public static net.minecraft.item.Item convert(Item item) {
        return castOrWrap(item, net.minecraft.item.Item.class, s -> null);
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

    public static Block.Settings convert(com.hrznstudio.sandbox.api.block.Block.Properties properties) {
        return castOrWrap(properties, Block.Settings.class, prop -> Block.Settings.of(convert(properties.getMaterial())));
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

    public static BlockView convert(WorldReader reader) {
        return castOrWrap(reader, BlockView.class, read -> null);
    }

    public static net.minecraft.block.entity.BlockEntity convert(BlockEntity entity) {
        return castOrWrap(entity, net.minecraft.block.entity.BlockEntity.class, read -> BlockEntityWrapper.create(entity));
    }

    public static BlockEntityType<?> convert(BlockEntity.Type<?> type) {
        return castOrWrap(type, BlockEntityType.class, t -> null);
    }
}