package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.api.block.entity.BlockEntityCTX;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.World;
import net.minecraft.block.entity.BlockEntity;

public class BlockEntityCTXWrapper implements BlockEntityCTX {
    private BlockEntity entity;

    BlockEntityCTXWrapper(BlockEntity entity) {
        this.entity = entity;
    }

    @Override
    public World getWorld() {
        return (World) entity.getWorld();
    }

    @Override
    public Position getPosition() {
        return (Position) entity.getPos();
    }

    @Override
    public void save() {
        entity.markDirty();
    }
}