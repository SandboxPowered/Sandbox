package com.hrznstudio.sandbox.mixin.common;

import com.hrznstudio.sandbox.event.block.BlockEvent;
import com.hrznstudio.sandbox.server.SandboxServer;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class MixinServerPlayerInteractionManager {

    @Shadow
    public ServerWorld world;

    @Inject(method = "tryBreakBlock", at = @At("HEAD"), cancellable = true)
    public void place(BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        BlockEvent.Break event = SandboxServer.INSTANCE.getDispatcher().publish(new BlockEvent.Break(world, pos, world.getBlockState(pos)));
        if (event.wasCancelled()) {
            info.setReturnValue(false);
        }
    }
}