package org.sandboxpowered.sandbox.fabric.mixin.event.network;

import org.sandboxpowered.sandbox.api.event.BlockEvent;
import org.sandboxpowered.sandbox.api.state.BlockState;
import org.sandboxpowered.sandbox.api.util.math.Position;
import org.sandboxpowered.sandbox.api.world.World;
import org.sandboxpowered.sandbox.fabric.event.EventDispatcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
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
        BlockEvent.Break event = EventDispatcher.publish(new BlockEvent.Break(
                (World) this.client.world,
                (Position) pos,
                (BlockState) this.client.world.getBlockState(pos)
        ));
        if (event.isCancelled()) {
            info.setReturnValue(false);
        }
    }
}