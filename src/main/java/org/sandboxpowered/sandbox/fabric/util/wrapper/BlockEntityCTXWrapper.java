package org.sandboxpowered.sandbox.fabric.util.wrapper;

import org.sandboxpowered.sandbox.api.block.entity.BlockEntityContext;
import org.sandboxpowered.sandbox.api.util.math.Position;
import org.sandboxpowered.sandbox.api.world.World;
import net.minecraft.block.entity.BlockEntity;

public class BlockEntityCTXWrapper implements BlockEntityContext {
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