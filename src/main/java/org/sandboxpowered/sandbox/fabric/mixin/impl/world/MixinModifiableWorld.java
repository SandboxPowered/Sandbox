package org.sandboxpowered.sandbox.fabric.mixin.impl.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableWorld;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.entity.Entity;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.BlockFlag;
import org.sandboxpowered.api.world.WorldWriter;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModifiableWorld.class)
public interface MixinModifiableWorld extends WorldWriter {
    @Shadow
    boolean setBlockState(BlockPos var1, net.minecraft.block.BlockState var2, int var3);

    @Shadow
    boolean breakBlock(BlockPos blockPos, boolean bl, @Nullable net.minecraft.entity.Entity entity);

    @Override
    default boolean setBlockState(Position position, BlockState state, BlockFlag... flags) {
        return setBlockState(WrappingUtil.convert(position), WrappingUtil.convert(state), WrappingUtil.convert(flags));
    }

    @Override
    default boolean breakBlock(Position position, boolean drop, @Nullable Entity entity) {

        return breakBlock(WrappingUtil.convert(position), drop, WrappingUtil.convert(entity));
    }
}