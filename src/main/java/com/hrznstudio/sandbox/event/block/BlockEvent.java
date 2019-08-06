package com.hrznstudio.sandbox.event.block;

import com.hrznstudio.sandbox.event.Event;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;

public class BlockEvent extends Event {
    @Cancellable
    public static class PlaceEvent extends BlockEvent {
        private final ItemPlacementContext context;
        private BlockState state;

        public PlaceEvent(ItemPlacementContext context, BlockState state) {
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
}
