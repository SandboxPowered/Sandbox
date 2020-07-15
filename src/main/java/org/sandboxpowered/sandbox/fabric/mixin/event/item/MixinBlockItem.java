package org.sandboxpowered.sandbox.fabric.mixin.event.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import org.sandboxpowered.api.events.BlockEvents;
import org.sandboxpowered.sandbox.fabric.impl.event.BlockModifiableArgsImpl;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
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
        BlockModifiableArgsImpl args = BlockModifiableArgsImpl.get(WrappingUtil.convert(context.getWorld()), WrappingUtil.convert(context.getBlockPos()), WrappingUtil.convert(state));

        BlockEvents.PLACE.accept(WrappingUtil.convert(context.getPlayer()), args);

        BlockState state2 = WrappingUtil.convert(args.getState());

        args.release();

        if (args.isCanceled()) {
            info.setReturnValue(false);
        } else if (state != state2) {
            info.setReturnValue(context.getWorld().setBlockState(context.getBlockPos(), state2, 11));
        }
    }
}