package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.api.block.entity.IBlockEntity;
import com.hrznstudio.sandbox.api.util.nbt.ReadableCompoundTag;
import com.hrznstudio.sandbox.api.util.nbt.WritableCompoundTag;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

import javax.annotation.Nonnull;

public class BlockEntityWrapper extends BlockEntity {
    private IBlockEntity blockEntity;

    public BlockEntityWrapper(@Nonnull IBlockEntity blockEntity) {
        super(WrappingUtil.convert(blockEntity.getType()));
        this.blockEntity = blockEntity;
        if (this.blockEntity instanceof com.hrznstudio.sandbox.api.block.entity.BlockEntity) {
            ((com.hrznstudio.sandbox.api.block.entity.BlockEntity) this.blockEntity).setCtx(new BlockEntityCTXWrapper(this));
        }
    }

    public static BlockEntityWrapper create(IBlockEntity blockEntity) {
        if (blockEntity instanceof IBlockEntity.Tickable)
            return new BlockEntityWrapper.Ticking((IBlockEntity.Tickable) blockEntity);
        return new BlockEntityWrapper(blockEntity);
    }

    public IBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public void fromTag(CompoundTag compoundTag_1) {
        super.fromTag(compoundTag_1);
        blockEntity.read((ReadableCompoundTag) compoundTag_1);
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag_1) {
        super.toTag(compoundTag_1);
        blockEntity.write((WritableCompoundTag) compoundTag_1);
        return compoundTag_1;
    }

    public static class Ticking extends BlockEntityWrapper implements Tickable {
        private IBlockEntity.Tickable tickableBlockEntity;

        public Ticking(IBlockEntity.Tickable blockEntity) {
            super(blockEntity);
            this.tickableBlockEntity = blockEntity;
        }

        @Override
        public void tick() {
            tickableBlockEntity.onTick();
        }
    }
}