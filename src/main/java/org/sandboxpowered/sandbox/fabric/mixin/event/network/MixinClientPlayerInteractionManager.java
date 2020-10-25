package org.sandboxpowered.sandbox.fabric.mixin.event.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private ClientPlayNetworkHandler networkHandler;

    @Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
    public void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        if (BlockEvents.BREAK.hasSubscribers()) {
            Cancellable cancellable = new Cancellable();
            World world = WrappingUtil.convert(this.client.world);
            Position position = WrappingUtil.convert(pos);
            BlockState state = WrappingUtil.convert(this.client.world.getBlockState(pos));
            PlayerEntity player = WrappingUtil.convert(this.client.player);
            ItemStack tool = player.getHeld(Hand.MAIN_HAND);

            BlockEvents.BREAK.post(breakEvent -> breakEvent.onEvent(world, position, state, player, tool, cancellable), cancellable);
            if (cancellable.isCancelled()) {
                info.setReturnValue(false);
            }
        }
    }

    @Inject(method = "interactBlock", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/hit/BlockHitResult;getBlockPos()Lnet/minecraft/util/math/BlockPos;"), cancellable = true)
    public void interactBlock(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, net.minecraft.util.Hand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<ActionResult> info) {
        if (BlockEvents.INTERACT.hasSubscribers()) {
            World sandWorld = WrappingUtil.convert(clientWorld);
            Position position = WrappingUtil.convert(blockHitResult.getBlockPos());
            BlockState sandState = sandWorld.getBlockState(position);
            PlayerEntity sandPlayer = WrappingUtil.convert(clientPlayerEntity);
            Hand sandHand = WrappingUtil.convert(hand);
            ItemStack tool = sandPlayer.getHeld(sandHand);

            InteractionResult result = BlockEvents.INTERACT.post((event, res) -> event.onEvent(sandWorld, position, sandState, sandPlayer, sandHand, tool, res), InteractionResult.IGNORE);

            if (result != InteractionResult.IGNORE) {
                this.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(hand, blockHitResult));
                info.setReturnValue(WrappingUtil.convert(result));
            }
        }
    }
}