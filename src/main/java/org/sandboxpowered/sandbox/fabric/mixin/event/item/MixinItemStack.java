package org.sandboxpowered.sandbox.fabric.mixin.event.item;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.sandboxpowered.sandbox.api.event.ItemEvent;
import org.sandboxpowered.sandbox.fabric.event.EventDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Random;

@Mixin(ItemStack.class)
public class MixinItemStack {
    @Inject(method = "damage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void place(int int_1, Random random_1, @Nullable ServerPlayerEntity serverPlayerEntity_1, CallbackInfoReturnable<Boolean> info) {
        ItemEvent.DamageItem event = EventDispatcher.publish(new ItemEvent.DamageItem((org.sandboxpowered.sandbox.api.item.ItemStack) this));
        if (event.isCancelled()) {
            info.setReturnValue(false);
        }
    }
}