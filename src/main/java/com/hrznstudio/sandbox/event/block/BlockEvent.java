package com.hrznstudio.sandbox.event.block;

import com.hrznstudio.sandbox.event.Event;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEvent extends Event {
    @Cancellable
    public static class Place extends BlockEvent {
        private final ItemPlacementContext context;
        private final BlockState state;

        public Place(ItemPlacementContext context, BlockState state) {
            this.context = context;
            this.state = state;
        }

        public ItemPlacementContext getContext() {
            return context;
        }

        public BlockState getState() {
            return state;
        }

    }
    @Cancellable
    public static class Break extends BlockEvent {
        private final World world;
        private final BlockPos pos;
        private final BlockState state;

        public Break(World world, BlockPos pos, BlockState state) {
            this.world = world;
            this.pos = pos;
            this.state = state;
        }

        public World getWorld() {
            return world;
        }

        public BlockPos getPos() {
            return pos;
        }

        public BlockState getState() {
            return state;
        }
    }
}
