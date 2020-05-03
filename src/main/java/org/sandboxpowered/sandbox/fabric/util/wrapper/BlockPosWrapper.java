package org.sandboxpowered.sandbox.fabric.util.wrapper;

import net.minecraft.util.math.BlockPos;
import org.sandboxpowered.api.util.math.Position;

public class BlockPosWrapper extends BlockPos {
    private final Position position;

    public BlockPosWrapper(Position position) {
        super(position.x(), position.y(), position.z());
        this.position = position;
    }

    @Override
    public int getX() {
        return position.x();
    }

    @Override
    public int getY() {
        return position.y();
    }

    @Override
    public int getZ() {
        return position.z();
    }

}
