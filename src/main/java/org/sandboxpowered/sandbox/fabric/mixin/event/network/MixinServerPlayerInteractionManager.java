package org.sandboxpowered.sandbox.fabric.mixin.event.network;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.sandboxpowered.api.events.BlockEvents;
import org.sandboxpowered.sandbox.fabric.impl.event.BlockArgsImpl;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class MixinServerPlayerInteractionManager {

    @Shadow
    public ServerWorld world;

    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "tryBreakBlock", at = @At("HEAD"), cancellable = true)
    public void tryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        BlockArgsImpl args = new BlockArgsImpl(WrappingUtil.convert(world), WrappingUtil.convert(pos), WrappingUtil.convert(world.getBlockState(pos)));

        BlockEvents.BREAK.accept(WrappingUtil.convert(player), args);

        if (args.isCanceled()) {
            info.setReturnValue(false);
        }
    }
}