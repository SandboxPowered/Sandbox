package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;

public class BlockEntityWrapper extends BlockEntity {
    private com.hrznstudio.sandbox.api.block.entity.BlockEntity blockEntity;

    public BlockEntityWrapper(com.hrznstudio.sandbox.api.block.entity.BlockEntity blockEntity) {
        super(WrappingUtil.convert(blockEntity.getType()));
        this.blockEntity = blockEntity;
    }

    public static BlockEntityWrapper create(com.hrznstudio.sandbox.api.block.entity.BlockEntity blockEntity) {
        return null;
    }

    public static class Ticking extends BlockEntityWrapper implements Tickable {
        private com.hrznstudio.sandbox.api.block.entity.BlockEntity.Tickable tickableBlockEntity;

        public Ticking(com.hrznstudio.sandbox.api.block.entity.BlockEntity blockEntity, com.hrznstudio.sandbox.api.block.entity.BlockEntity.Tickable tickableBlockEntity) {
            super(blockEntity);
            this.tickableBlockEntity = tickableBlockEntity;
        }

        @Override
        public void tick() {
            tickableBlockEntity.onTick();
        }
    }
}
