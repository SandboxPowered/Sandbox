package com.hrznstudio.sandbox.mixin.event.network;

import com.hrznstudio.sandbox.api.block.state.BlockState;
import com.hrznstudio.sandbox.api.event.BlockEvent;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.World;
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
    public void tryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        BlockEvent.Break event = SandboxServer.INSTANCE.dispatcher.publish(new BlockEvent.Break(
                (World) world,
                (Position) pos,
                (BlockState) world.getBlockState(pos)
        ));
        if (event.isCancelled()) {
            info.setReturnValue(false);
        }
    }
}