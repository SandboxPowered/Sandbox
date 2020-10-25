package org.sandboxpowered.sandbox.fabric.mixin.event.network;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.sandboxpowered.api.entity.player.Hand;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.sandboxpowered.api.events.BlockEvents;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.InteractionResult;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.eventhandler.Cancellable;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class MixinServerPlayerInteractionManager {

    @Shadow
    public ServerWorld world;

    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "tryBreakBlock", at = @At("HEAD"), cancellable = true)
    public void tryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        if (BlockEvents.BREAK.hasSubscribers()) {
            Cancellable cancellable = new Cancellable();
            World world = WrappingUtil.convert(this.world);
            Position position = WrappingUtil.convert(pos);
            BlockState state = WrappingUtil.convert(this.world.getBlockState(pos));
            PlayerEntity player = WrappingUtil.convert(this.player);
            ItemStack tool = player.getHeld(Hand.MAIN_HAND);

            BlockEvents.BREAK.post(breakEvent -> breakEvent.onEvent(world, position, state, player, tool, cancellable), cancellable);
            if (cancellable.isCancelled()) {
                info.setReturnValue(false);
            }
        }
    }

    @Inject(method = "interactBlock", at = @At(value = "HEAD"), cancellable = true)
    public void interactBlock(ServerPlayerEntity serverPlayerEntity, net.minecraft.world.World world, net.minecraft.item.ItemStack itemStack, net.minecraft.util.Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> info) {
        if (BlockEvents.INTERACT.hasSubscribers()) {
            World sandWorld = WrappingUtil.convert(world);
            Position position = WrappingUtil.convert(blockHitResult.getBlockPos());
            BlockState sandState = sandWorld.getBlockState(position);
            PlayerEntity sandPlayer = WrappingUtil.convert(serverPlayerEntity);
            ItemStack tool = WrappingUtil.convert(itemStack);
            Hand sandHand = WrappingUtil.convert(hand);

            InteractionResult result = BlockEvents.INTERACT.post((event, res) -> event.onEvent(sandWorld, position, sandState, sandPlayer, sandHand, tool, res), InteractionResult.IGNORE);

            if (result != InteractionResult.IGNORE) {
                info.setReturnValue(WrappingUtil.convert(result));
            }
        }
    }
}