package com.hrznstudio.sandbox.mixin.event.enchant;

import com.hrznstudio.sandbox.api.event.EnchantmentEvent;
import com.hrznstudio.sandbox.api.util.InteractionResult;
import com.hrznstudio.sandbox.server.SandboxServer;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class MixinEnchantment {

    @Inject(method = "isAcceptableItem", at = @At(value = "HEAD"), cancellable = true)
    public void place(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        EnchantmentEvent.AcceptableItem event = SandboxServer.INSTANCE.dispatcher.publish(new EnchantmentEvent.AcceptableItem((com.hrznstudio.sandbox.api.enchant.Enchantment) (Object) this, WrappingUtil.cast(stack, com.hrznstudio.sandbox.api.item.ItemStack.class)));
        if (event.getResult() != InteractionResult.IGNORE) {
            info.setReturnValue(event.getResult() == InteractionResult.SUCCESS);
        }
    }
}