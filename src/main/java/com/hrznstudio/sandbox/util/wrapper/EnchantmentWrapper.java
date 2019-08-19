package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentWrapper extends Enchantment {
    private final com.hrznstudio.sandbox.api.enchant.Enchantment enchantment;

    public EnchantmentWrapper(com.hrznstudio.sandbox.api.enchant.Enchantment enchantment) {
        super(Weight.COMMON, EnchantmentTarget.ALL, EquipmentSlot.values()); //TODO: Allow sandbox enchants to specify these values
        this.enchantment = enchantment;
    }

    @Override
    public int getMinimumLevel() {
        return enchantment.getMinimumLevel();
    }

    @Override
    public int getMaximumLevel() {
        return enchantment.getMaximumLevel();
    }

    @Override
    public boolean isAcceptableItem(ItemStack itemStack_1) {
        return enchantment.isAcceptableItem(WrappingUtil.cast(itemStack_1, com.hrznstudio.sandbox.api.item.ItemStack.class));
    }

    @Override
    public boolean isTreasure() {
        return enchantment.isTreasure();
    }

    @Override
    public boolean isCursed() {
        return enchantment.isCurse();
    }
}
