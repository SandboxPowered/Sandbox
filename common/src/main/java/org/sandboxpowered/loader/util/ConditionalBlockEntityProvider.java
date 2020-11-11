package org.sandboxpowered.loader.util;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface ConditionalBlockEntityProvider {
    boolean hasBlockEntity(BlockState state);

    @Nullable
    BlockEntity createBlockEntity(BlockGetter getter, BlockState state);
}
