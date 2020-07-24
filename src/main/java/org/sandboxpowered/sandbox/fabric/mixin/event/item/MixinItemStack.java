package org.sandboxpowered.sandbox.fabric.mixin.event.item;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.sandboxpowered.api.events.ItemEvents;
import org.sandboxpowered.api.events.args.ItemArgs;
import org.sandboxpowered.sandbox.fabric.impl.event.ItemArgsImpl;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
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
        ItemArgs args = new ItemArgsImpl(WrappingUtil.convert((ItemStack) (Object) this));

        ItemEvents.DAMAGE.accept(WrappingUtil.convert(serverPlayerEntity_1), args);

        if (args.isCanceled())
            info.setReturnValue(false);
    }
}