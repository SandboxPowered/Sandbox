package org.sandboxpowered.sandbox.fabric.mixin.impl.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.entity.Entity;
import org.sandboxpowered.api.shape.Box;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.state.FluidState;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.WorldReader;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.stream.Stream;

@Mixin(BlockView.class)
public interface MixinBlockView extends WorldReader {
    @Shadow
    net.minecraft.block.BlockState getBlockState(BlockPos blockPos_1);

    @Shadow
    @Nullable
    net.minecraft.block.entity.BlockEntity getBlockEntity(BlockPos var1);

    @Shadow
    net.minecraft.fluid.FluidState getFluidState(BlockPos blockPos);

    @Override
    default BlockState getBlockState(Position position) {
        return WrappingUtil.convert(this.getBlockState(WrappingUtil.convert(position)));
    }

    @Override
    default FluidState getFluidState(Position position) {
        return WrappingUtil.convert(getFluidState(WrappingUtil.convert(position)));
    }

    @Override
    @Nullable
    default BlockEntity getBlockEntity(Position position) {
        return WrappingUtil.convert(this.getBlockEntity(WrappingUtil.convert(position)));
    }

    @Override
    default Stream<Entity> getEntitiesWithin(Box box) {
        return Stream.empty();
    }

    @Override
    default <T extends Entity> Stream<T> getEntitiesWithin(Box box, Class<T> filter) {
        return Stream.empty();
    }
}