package org.sandboxpowered.sandbox.fabric.impl.event;

import org.sandboxpowered.api.events.args.BlockModifiableArgs;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.eventhandler.CancellableEventArgs;

import java.util.ArrayList;
import java.util.List;

public class BlockModifiableArgsImpl extends CancellableEventArgs implements BlockModifiableArgs {
    private static final List<BlockModifiableArgsImpl> POOL = new ArrayList<>();
    private World world;
    private Position position;
    private BlockState state;

    public static BlockModifiableArgsImpl get(World world, Position position, BlockState state) {
        BlockModifiableArgsImpl args;
        if (POOL.isEmpty()) {
            args = new BlockModifiableArgsImpl();
        } else {
            args = POOL.remove(0);
        }
        args.setup(world, position, state);
        return args;
    }

    private void setup(World world, Position position, BlockState state) {
        this.world = world;
        this.position = position;
        this.state = state;
        reset();
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public BlockState getState() {
        return state;
    }

    @Override
    public void setState(BlockState state) {
        this.state = state;
    }

    public void release() {
        POOL.add(this);
    }
}