package org.sandboxpowered.sandbox.fabric.mixin.impl.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.WorldReader;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(BlockView.class)
public interface MixinBlockView extends WorldReader {
    @Shadow
    net.minecraft.block.BlockState getBlockState(BlockPos blockPos_1);

    @Shadow
    @Nullable
    net.minecraft.block.entity.BlockEntity getBlockEntity(BlockPos var1);

    @Override
    default BlockState getBlockState(Position position) {
        return (BlockState) this.getBlockState(WrappingUtil.convert(position));
    }

    @Override
    @Nullable
    default BlockEntity getBlockEntity(Position position) {
        return (BlockEntity) this.getBlockEntity(WrappingUtil.convert(position));
    }
}