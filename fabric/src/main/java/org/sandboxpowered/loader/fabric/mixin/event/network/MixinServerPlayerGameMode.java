package org.sandboxpowered.loader.fabric.mixin.event.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.sandboxpowered.api.entity.player.Hand;
import org.sandboxpowered.api.events.BlockEvents;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.InteractionResult;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.eventhandler.Cancellable;
import org.sandboxpowered.loader.Wrappers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class MixinServerPlayerGameMode {
    @Shadow
    public ServerLevel level;

    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    public void tryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        if (BlockEvents.BREAK.hasSubscribers()) {
            Cancellable cancellable = new Cancellable();
            World world = Wrappers.WORLD.toSandbox(level);
            Position position = Wrappers.POSITION.toSandbox(pos);
            BlockState state = world.getBlockState(position);
            ItemStack tool = ItemStack.empty();

            BlockEvents.BREAK.post(breakEvent -> breakEvent.onEvent(world, position, state, null, tool, cancellable), cancellable);
            if (cancellable.isCancelled()) {
                info.setReturnValue(false);
            }
        }
    }

    @Inject(method = "useItemOn", at = @At(value = "HEAD"), cancellable = true)
    public void interactBlock(ServerPlayer serverPlayer, Level level, net.minecraft.world.item.ItemStack itemStack, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<net.minecraft.world.InteractionResult> info) {
        if (BlockEvents.INTERACT.hasSubscribers()) {
            World world = Wrappers.WORLD.toSandbox(level);
            Position position = Wrappers.POSITION.toSandbox(blockHitResult.getBlockPos());
            BlockState blockstate = world.getBlockState(position);
            ItemStack tool = ItemStack.empty();
            Hand sandHand = interactionHand == InteractionHand.MAIN_HAND ? Hand.MAIN_HAND : Hand.OFF_HAND;

            InteractionResult result = BlockEvents.INTERACT.post((event, res) -> event.onEvent(world, position, blockstate, null, sandHand, tool, res), InteractionResult.IGNORE);

            if (result != InteractionResult.IGNORE) {
                info.setReturnValue(Wrappers.INTERACTION_RESULT.toVanilla(result));
            }
        }
    }
}