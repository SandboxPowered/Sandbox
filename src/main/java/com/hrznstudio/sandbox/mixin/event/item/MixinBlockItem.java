package com.hrznstudio.sandbox.mixin.event.item;

import com.hrznstudio.sandbox.api.event.BlockEvent;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.World;
import com.hrznstudio.sandbox.event.EventDispatcher;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class MixinBlockItem {
    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void place(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> info) {
        BlockEvent.Place event = EventDispatcher.publish(new BlockEvent.Place(
                (World) context.getWorld(),
                (Position) context.getBlockPos(),
                (com.hrznstudio.sandbox.api.state.BlockState) state
        ));
        BlockState state2 = WrappingUtil.convert(event.getState());
        if (event.isCancelled()) {
            info.setReturnValue(false);
        } else if (state2 != state) {
            info.setReturnValue(context.getWorld().setBlockState(context.getBlockPos(), state2, 11));
        }
    }
}