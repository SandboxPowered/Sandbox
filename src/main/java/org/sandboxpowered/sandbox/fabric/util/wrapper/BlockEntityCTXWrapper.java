package org.sandboxpowered.sandbox.fabric.util.wrapper;

import net.minecraft.block.entity.BlockEntity;
import org.sandboxpowered.internal.BlockEntityContext;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.World;

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