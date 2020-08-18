package org.sandboxpowered.sandbox.fabric.mixin.event.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.sandboxpowered.api.events.BlockEvents;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.eventhandler.Cancellable;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(BlockItem.class)
public abstract class MixinBlockItem {
    private final AtomicReference<BlockState> stateReference = new AtomicReference<>();

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z",
            at = @At(value = "HEAD"), cancellable = true
    )
    public void place(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> info) {
        if (BlockEvents.PLACE.hasSubscribers()) {
            Cancellable cancellable = new Cancellable();
            World world = WrappingUtil.convert(context.getWorld());
            Position pos = WrappingUtil.convert(context.getBlockPos());
            org.sandboxpowered.api.state.BlockState originalState = WrappingUtil.convert(state);
            PlayerEntity player = WrappingUtil.convert(context.getPlayer());
            ItemStack stack = WrappingUtil.convert(context.getStack());
            org.sandboxpowered.api.state.BlockState retState = BlockEvents.PLACE.post(
                    (event, value) -> event.onEvent(world, pos, value, player, stack, cancellable),
                    originalState,
                    cancellable
            );
            if (cancellable.isCancelled()) info.setReturnValue(false);
            else stateReference.set(WrappingUtil.convert(retState));
        } else {
            stateReference.set(state);
        }
    }

    @Redirect(method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z")
    )
    public boolean setBlockState(net.minecraft.world.World world, BlockPos blockPos, BlockState blockState, int i) {
        BlockState state = stateReference.getAndSet(null);
        if (state != null)
            return world.setBlockState(blockPos, state, i);
        return world.setBlockState(blockPos, blockState, i);
    }
}