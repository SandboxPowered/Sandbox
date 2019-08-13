package com.hrznstudio.sandbox.util;

import com.hrznstudio.sandbox.api.block.state.BlockState;
import com.hrznstudio.sandbox.api.item.Item;
import com.hrznstudio.sandbox.api.util.Direction;
import com.hrznstudio.sandbox.api.util.Identity;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.BlockFlag;
import com.hrznstudio.sandbox.util.wrapper.BlockPosWrapper;
import com.hrznstudio.sandbox.util.wrapper.BlockWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.function.Function;

public class ConversionUtil {

    public static BlockPos convert(Position position) {
        return castOrWrap(position, BlockPos.class, p -> new BlockPosWrapper(position));
    }

    public static net.minecraft.block.BlockState convert(BlockState state) {
        return castOrWrap(state, net.minecraft.block.BlockState.class, s -> null);
    }

    public static Block convert(com.hrznstudio.sandbox.api.block.Block block) {
        return castOrWrap(block, Block.class, s -> new BlockWrapper(block));
    }

    public static net.minecraft.item.Item convert(Item item) {
        return castOrWrap(item, net.minecraft.item.Item.class, s -> null);
    }

    private static <A, B> B castOrWrap(A a, Class<B> bClass, Function<A, B> wrapper) {
        if (bClass.isInstance(a))
            return bClass.cast(a);
        return wrapper.apply(a);
    }

    public static Block.Settings convert(com.hrznstudio.sandbox.api.block.Block.Properties properties) {
        return castOrWrap(properties, Block.Settings.class, prop -> Block.Settings.of(Material.METAL));
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
}