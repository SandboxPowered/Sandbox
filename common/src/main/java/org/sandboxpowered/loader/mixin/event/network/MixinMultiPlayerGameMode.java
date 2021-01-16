package org.sandboxpowered.loader.mixin.event.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MixinMultiPlayerGameMode {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    public void tryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        Level level = minecraft.level;
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
    public void interactBlock(LocalPlayer localPlayer, ClientLevel clientLevel, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<net.minecraft.world.InteractionResult> info) {
        if (BlockEvents.INTERACT.hasSubscribers()) {
            World world = Wrappers.WORLD.toSandbox(clientLevel);
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