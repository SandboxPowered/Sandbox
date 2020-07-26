package org.sandboxpowered.sandbox.fabric.mixin.event.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.sandboxpowered.api.events.EnchantmentEvents;
import org.sandboxpowered.api.events.ItemEvents;
import org.sandboxpowered.api.events.args.Result;
import org.sandboxpowered.sandbox.fabric.impl.event.ItemResultArgsImpl;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.enchantment.Enchantment.class)
public abstract class MixinEnchantment {

    @Inject(method = "isAcceptableItem", at = @At(value = "HEAD"), cancellable = true)
    public void isAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        ItemResultArgsImpl args = new ItemResultArgsImpl(WrappingUtil.convert(stack));

        EnchantmentEvents.VALID_ITEM.accept(WrappingUtil.convert(WrappingUtil.cast(this, Enchantment.class)), args);

        if (args.getResult() != Result.IGNORE) {
            info.setReturnValue(args.getResult() == Result.SUCCESS);
        }
    }

    @Inject(method = "canCombine", at = @At(value = "HEAD"), cancellable = true)
    public void isDifferent(net.minecraft.enchantment.Enchantment other, CallbackInfoReturnable<Boolean> info) {
//        EnchantmentEvent.Compatible event = EventDispatcher.publish(new EnchantmentEvent.Compatible((Enchantment) this, (Enchantment) other));
//        if (event.getResult() != EventResult.IGNORE) {
//            info.setReturnValue(event.getResult() == EventResult.SUCCESS);
//        }
    }
}