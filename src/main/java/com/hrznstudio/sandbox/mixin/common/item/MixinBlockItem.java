package com.hrznstudio.sandbox.mixin.common.item;

import com.hrznstudio.sandbox.event.EventDispatcher;
import com.hrznstudio.sandbox.event.block.BlockEvent;
import com.hrznstudio.sandbox.server.SandboxServer;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class MixinBlockItem {

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void place(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> info) {
        if (!context.getWorld().isClient) {
            BlockEvent.PlaceEvent event = SandboxServer.INSTANCE.getDispatcher().publish(new BlockEvent.PlaceEvent(context, state));
            if (event.wasCancelled()) {
                info.setReturnValue(false);
            } else {

            }
        }
    }
}