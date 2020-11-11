package org.sandboxpowered.loader.fabric.mixin.injection.hasBlockEntity;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.loader.util.ConditionalBlockEntityProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public class MixinBlock implements ConditionalBlockEntityProvider {
    @Override
    public boolean hasBlockEntity(BlockState state) {
        return this instanceof EntityBlock;
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockGetter getter, BlockState state) {
        if (this instanceof EntityBlock)
            return ((EntityBlock) this).newBlockEntity(getter);
        return null;
    }
}
