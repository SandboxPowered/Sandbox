package org.sandboxpowered.sandbox.fabric.util.wrapper;

import org.sandboxpowered.sandbox.api.util.math.Position;
import net.minecraft.util.math.BlockPos;

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

}
