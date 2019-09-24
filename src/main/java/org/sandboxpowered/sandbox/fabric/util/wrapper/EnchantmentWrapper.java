package org.sandboxpowered.sandbox.fabric.util.wrapper;

import org.sandboxpowered.sandbox.api.enchant.Enchantment;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class EnchantmentWrapper extends net.minecraft.enchantment.Enchantment {
    private final Enchantment enchantment;

    public EnchantmentWrapper(Enchantment enchantment) {
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
        return enchantment.isAcceptableItem(WrappingUtil.cast(itemStack_1, org.sandboxpowered.sandbox.api.item.ItemStack.class));
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
