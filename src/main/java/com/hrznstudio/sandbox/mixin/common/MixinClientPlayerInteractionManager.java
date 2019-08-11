package com.hrznstudio.sandbox.mixin.common;

import com.hrznstudio.sandbox.event.block.BlockEvent;
import com.hrznstudio.sandbox.server.SandboxServer;
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
    public void place(BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        BlockEvent.Break event = SandboxServer.INSTANCE.getDispatcher().publish(new BlockEvent.Break(this.client.world, pos, this.client.world.getBlockState(pos)));
        if (event.wasCancelled()) {
            info.setReturnValue(false);
        }
    }
}