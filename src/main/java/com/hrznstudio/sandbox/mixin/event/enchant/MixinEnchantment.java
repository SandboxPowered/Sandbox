package com.hrznstudio.sandbox.mixin.event.enchant;

import com.hrznstudio.sandbox.api.enchant.IEnchantment;
import com.hrznstudio.sandbox.api.event.EnchantmentEvent;
import com.hrznstudio.sandbox.api.event.EventResult;
import com.hrznstudio.sandbox.event.EventDispatcher;
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
    public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        EnchantmentEvent.AcceptableItem event = EventDispatcher.publish(new EnchantmentEvent.AcceptableItem((IEnchantment) this, WrappingUtil.cast(stack, com.hrznstudio.sandbox.api.item.ItemStack.class)));
        if (event.getResult() != EventResult.IGNORE) {
            info.setReturnValue(event.getResult() == EventResult.SUCCESS);
        }
    }

    @Inject(method = "isDifferent", at = @At(value = "HEAD"), cancellable = true)
    public void isDifferent(Enchantment other, CallbackInfoReturnable<Boolean> info) {
        EnchantmentEvent.Compatible event = EventDispatcher.publish(new EnchantmentEvent.Compatible((IEnchantment) this, (IEnchantment) other));
        if (event.getResult() != EventResult.IGNORE) {
            info.setReturnValue(event.getResult() == EventResult.SUCCESS);
        }
    }
}