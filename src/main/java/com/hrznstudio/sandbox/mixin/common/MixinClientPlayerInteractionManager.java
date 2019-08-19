package com.hrznstudio.sandbox.mixin.common;

import com.hrznstudio.sandbox.api.block.state.BlockState;
import com.hrznstudio.sandbox.api.event.BlockEvent;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.World;
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
    public void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        BlockEvent.Break event = SandboxServer.INSTANCE.dispatcher.publish(new BlockEvent.Break(
                (World) this.client.world,
                (Position) pos,
                (BlockState) this.client.world.getBlockState(pos)
        ));
        if (event.isCancelled()) {
            info.setReturnValue(false);
        }
    }
}