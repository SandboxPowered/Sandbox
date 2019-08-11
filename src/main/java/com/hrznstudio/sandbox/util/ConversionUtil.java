package com.hrznstudio.sandbox.util;

import com.hrznstudio.sandbox.api.block.state.BlockState;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.util.wrapper.BlockPosWrapper;
import net.minecraft.util.math.BlockPos;

import java.util.function.Function;

public class ConversionUtil {

    public static BlockPos convertPosition(Position position) {
        return castOrWrap(position, BlockPos.class, p -> new BlockPosWrapper(position));
    }

    public static net.minecraft.block.BlockState convertBlockState(BlockState state) {
        return castOrWrap(state, net.minecraft.block.BlockState.class, s -> null);
    }

    private static <A, B> B castOrWrap(A a, Class<B> bClass, Function<A, B> wrapper) {
        if (bClass.isInstance(a))
            return bClass.cast(a);
        return wrapper.apply(a);
    }
}