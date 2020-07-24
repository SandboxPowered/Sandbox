package org.sandboxpowered.sandbox.fabric.impl.event;

import org.sandboxpowered.api.events.args.BlockArgs;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.eventhandler.CancellableEventArgs;

public class BlockArgsImpl extends CancellableEventArgs implements BlockArgs {
    private final World world;
    private final Position position;
    private final BlockState state;

    public BlockArgsImpl(World world, Position position, BlockState state) {
        this.world = world;
        this.position = position;
        this.state = state;
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
}