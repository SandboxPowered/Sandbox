package com.hrznstudio.sandbox.mixin.common.world;

import com.hrznstudio.sandbox.api.block.state.BlockState;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.WorldReader;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockView.class)
public interface MixinBlockView extends WorldReader {
    @Shadow
    net.minecraft.block.BlockState getBlockState(BlockPos blockPos_1);

    @Override
    default BlockState getBlockState(Position position) {
        return (BlockState) this.getBlockState(WrappingUtil.convert(position));
    }

}