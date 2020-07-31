package org.sandboxpowered.sandbox.fabric.mixin.event.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.sandboxpowered.api.entity.player.Hand;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.sandboxpowered.api.events.BlockEvents;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.state.BlockState;
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

    @Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
    public void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        if (BlockEvents.BREAK.hasSubscribers()) {
            Cancellable cancellable = new Cancellable();
            World world = WrappingUtil.convert(this.client.world);
            Position position = WrappingUtil.convert(pos);
            BlockState state = WrappingUtil.convert(this.client.world.getBlockState(pos));
            PlayerEntity player = WrappingUtil.convert(this.client.player);
            ItemStack tool = player.getHeldItem(Hand.MAIN_HAND);

            BlockEvents.BREAK.post(breakEvent -> breakEvent.onEvent(world, position, state, player, tool, cancellable), cancellable);
            if (cancellable.isCancelled()) {
                info.setReturnValue(false);
            }
        }
    }
}