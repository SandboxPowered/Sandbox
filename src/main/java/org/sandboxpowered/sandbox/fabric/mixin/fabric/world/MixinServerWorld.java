package org.sandboxpowered.sandbox.fabric.mixin.fabric.world;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld {
    @Redirect(method = "updateSleepingPlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;isSpectator()Z"))
    public boolean isSpectator(ServerPlayerEntity entity) {
        return entity.isSpectator() || WrappingUtil.convert(entity).isSleepingIgnored();
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;noneMatch(Ljava/util/function/Predicate;)Z"))
    public boolean noneMatch(Stream<ServerPlayerEntity> stream, Predicate<ServerPlayerEntity> predicate) {
        return stream.noneMatch(predicate.and(entity -> !WrappingUtil.convert(entity).isSleepingIgnored()));
    }
}