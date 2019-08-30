package com.hrznstudio.sandbox.mixin.fabric.enchant;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.InfoEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Iterator;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper {

    /**
     * @author Coded
     * @reason mojang was dumb
     */
    @Overwrite
    public static List<InfoEnchantment> getHighestApplicableEnchantmentsAtPower(int int_1, ItemStack itemStack_1, boolean treasureAllowed) {
        List<InfoEnchantment> list_1 = Lists.newLinkedList();
        boolean boolean_2 = itemStack_1.getItem() == Items.BOOK;
        Iterator var6 = Registry.ENCHANTMENT.iterator();

        while (true) {
            while (true) {
                Enchantment enchantment_1;
                do {
                    do {
                        if (!var6.hasNext()) {
                            return list_1;
                        }

                        enchantment_1 = (Enchantment) var6.next();
                    } while (enchantment_1.isTreasure() && !treasureAllowed);
                } while (!enchantment_1.isAcceptableItem(itemStack_1) && !boolean_2);

                for (int int_2 = enchantment_1.getMaximumLevel(); int_2 > enchantment_1.getMinimumLevel() - 1; --int_2) {
                    if (int_1 >= enchantment_1.getMinimumPower(int_2) && int_1 <= enchantment_1.method_20742(int_2)) {
                        list_1.add(new InfoEnchantment(enchantment_1, int_2));
                        break;
                    }
                }
            }
        }
    }
}