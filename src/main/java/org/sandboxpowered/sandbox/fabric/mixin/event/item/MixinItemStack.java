package org.sandboxpowered.sandbox.fabric.mixin.event.item;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.sandboxpowered.api.events.ItemEvents;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Random;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @Shadow
    public abstract boolean isDamageable();

    @ModifyArg(method = "damage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", at = @At(value = "HEAD"), index = 0)
    public int place(int int_1, Random random_1, @Nullable ServerPlayerEntity serverPlayerEntity_1) {
        if (isDamageable())
            return int_1;
        if (ItemEvents.DAMAGE.hasSubscribers()) {
            PlayerEntity player = WrappingUtil.convert(serverPlayerEntity_1);
            org.sandboxpowered.api.item.ItemStack stack = WrappingUtil.convert((ItemStack) (Object) this);
            return ItemEvents.DAMAGE.post((event, value) -> event.onEvent(player, stack, value), int_1);
        }
        return int_1;
    }
}