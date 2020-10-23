package org.sandboxpowered.sandbox.fabric.util.wrapper;

import net.minecraft.util.math.BlockPos;
import org.sandboxpowered.api.util.math.Position;

public class BlockPosWrapper extends BlockPos {
    private final Position position;

    public BlockPosWrapper(Position position) {
        super(position.getX(), position.getY(), position.getZ());
        this.position = position;
    }

    @Override
    public int getX() {
        return position.getX();
    }

    @Override
    public int getY() {
        return position.getY();
    }

    @Override
    public int getZ() {
        return position.getZ();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof BlockPos)
            return position.equals(o);
        return false;
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }
}
