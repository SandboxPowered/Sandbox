package org.sandboxpowered.sandbox.fabric.util.wrapper;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import org.jetbrains.annotations.NotNull;
import org.sandboxpowered.api.block.entity.BaseBlockEntity;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.util.nbt.ReadableCompoundTag;
import org.sandboxpowered.api.util.nbt.WritableCompoundTag;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

public class BlockEntityWrapper extends net.minecraft.block.entity.BlockEntity {
    private final BlockEntity blockEntity;

    public BlockEntityWrapper(@NotNull BlockEntity blockEntity) {
        super(WrappingUtil.convert(blockEntity.getType()));
        this.blockEntity = blockEntity;
        if (this.blockEntity instanceof BaseBlockEntity) {
            ((BaseBlockEntity) this.blockEntity).setContext(new BlockEntityCTXWrapper(this));
        }
    }

    public static BlockEntityWrapper create(BlockEntity blockEntity) {
        if (blockEntity instanceof BlockEntity.Tickable)
            return new BlockEntityWrapper.Ticking((BlockEntity.Tickable) blockEntity);
        return new BlockEntityWrapper(blockEntity);
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public void fromTag(BlockState blockState, CompoundTag tag) {
        super.fromTag(blockState, tag);
        blockEntity.read((ReadableCompoundTag) tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        blockEntity.write((WritableCompoundTag) tag);
        return tag;
    }

    public static class Ticking extends BlockEntityWrapper implements Tickable {
        private final BlockEntity.Tickable tickableBlockEntity;

        public Ticking(BlockEntity.Tickable blockEntity) {
            super(blockEntity);
            this.tickableBlockEntity = blockEntity;
        }

        @Override
        public void tick() {
            tickableBlockEntity.onTick();
        }
    }
}