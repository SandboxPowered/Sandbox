package org.sandboxpowered.loader.fabric.mixin.fixes.enchantment_acceptable_item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

/**
 * Fixes EnchantmentHelper bypassing Enchantment#canEnchant
 */
@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {
    private static final ThreadLocal<Enchantment> ENCHANTMENT_THREAD_LOCAL = new ThreadLocal<>();

    @Inject(method = "getAvailableEnchantmentResults", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;isTreasureOnly()Z"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private static void localEnchantment(int power, ItemStack stack, boolean allowTreasure, CallbackInfoReturnable<EnchantmentInstance> info, List<EnchantmentInstance> ret, Item item, boolean isBook, Iterator<Enchantment> itr, Enchantment enchantment) {
        ENCHANTMENT_THREAD_LOCAL.set(enchantment);
    }

    @Redirect(method = "getAvailableEnchantmentResults", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentCategory;canEnchant(Lnet/minecraft/world/item/Item;)Z"))
    private static boolean isAcceptableItem(EnchantmentCategory target, Item item, int i, ItemStack itemStack, boolean bl) {
        Enchantment enchantment = ENCHANTMENT_THREAD_LOCAL.get();
        ENCHANTMENT_THREAD_LOCAL.remove();
        if (enchantment == null) {
            return target.canEnchant(item);
        }
        return enchantment.canEnchant(itemStack);
    }
}
