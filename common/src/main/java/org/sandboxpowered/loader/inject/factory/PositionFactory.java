package org.sandboxpowered.loader.inject.factory;

import net.minecraft.core.BlockPos;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.loader.Wrappers;

public class PositionFactory implements Position.Factory{
    @Override
    public Position immutable(int x, int y, int z) {
        return Wrappers.POSITION.toSandbox(new BlockPos(x,y,z));
    }

    @Override
    public Position.Mutable mutable(int x, int y, int z) {
        return Wrappers.MUTABLE_POSITION.toSandbox(new BlockPos.MutableBlockPos(x,y,z));
    }
}
