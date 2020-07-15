package org.sandboxpowered.sandbox.fabric.impl.event;

import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.events.args.BlockArgs;
import org.sandboxpowered.api.events.args.BlockModifiableArgs;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockModifiableArgsImpl implements BlockModifiableArgs {
    private static List<BlockModifiableArgsImpl> POOL = new ArrayList<>();
    public static BlockModifiableArgsImpl get(World world, Position position, BlockState state) {
        BlockModifiableArgsImpl args;
        if(POOL.isEmpty()) {
            args= new BlockModifiableArgsImpl();
        } else {
            args=POOL.remove(0);
        }
        args.setup(world,position,state);
        return args;
    }

    private World world;
    private Position position;
    private BlockState state;
    private boolean cancelled;

    private void setup(World world, Position position, BlockState state) {
        this.world = world;
        this.position = position;
        this.state = state;
        cancelled = false;
    }

    @Override
    public void setState(BlockState state) {
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

    public void release() {
        POOL.add(this);
    }

    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public boolean isCanceled() {
        return cancelled;
    }
}