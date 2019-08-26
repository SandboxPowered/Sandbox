package com.hrznstudio.sandbox.mixin.impl.world;

import com.hrznstudio.sandbox.api.block.entity.IBlockEntity;
import com.hrznstudio.sandbox.api.block.state.BlockState;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.WorldReader;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(BlockView.class)
public interface MixinBlockView extends WorldReader {
    @Shadow
    net.minecraft.block.BlockState getBlockState(BlockPos blockPos_1);

    @Shadow
    @Nullable
    BlockEntity getBlockEntity(BlockPos var1);

    @Override
    default BlockState getBlockState(Position position) {
        return (BlockState) this.getBlockState(WrappingUtil.convert(position));
    }

    @Override
    default IBlockEntity getBlockEntity(Position position) {
        return (IBlockEntity) this.getBlockEntity(WrappingUtil.convert(position));
    }
}