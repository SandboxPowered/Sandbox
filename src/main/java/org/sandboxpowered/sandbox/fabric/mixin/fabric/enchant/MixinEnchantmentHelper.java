package org.sandboxpowered.sandbox.fabric.mixin.fabric.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper {

    private static final ThreadLocal<Enchantment> ENCHANTMENT_THREAD_LOCAL = new ThreadLocal<>();

    @Inject(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;isTreasure()Z"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void localEnchantment(int power, ItemStack stack, boolean allowTreasure, CallbackInfoReturnable<EnchantmentLevelEntry> info, List<EnchantmentLevelEntry> ret, Item item, boolean isBook, Iterator<Enchantment> itr, Enchantment enchantment) {
        ENCHANTMENT_THREAD_LOCAL.set(enchantment);
    }

    @Redirect(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"))
    private static boolean isAcceptableItem(EnchantmentTarget target, Item item, int i, ItemStack itemStack, boolean bl) {
        Enchantment enchantment = ENCHANTMENT_THREAD_LOCAL.get();
        ENCHANTMENT_THREAD_LOCAL.remove();
        if (enchantment == null) {
            return target.isAcceptableItem(item);
        }
        return enchantment.isAcceptableItem(itemStack);
    }
}