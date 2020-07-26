package org.sandboxpowered.sandbox.fabric.mixin.event.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.sandboxpowered.api.events.BlockEvents;
import org.sandboxpowered.sandbox.fabric.impl.event.BlockArgsImpl;
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
        BlockArgsImpl args = new BlockArgsImpl(WrappingUtil.convert(client.world), WrappingUtil.convert(pos), WrappingUtil.convert(this.client.world.getBlockState(pos)));

        BlockEvents.BREAK.accept(WrappingUtil.convert(client.player), args);

        if(args.isCanceled()) {
            info.setReturnValue(false);
        }
    }
}