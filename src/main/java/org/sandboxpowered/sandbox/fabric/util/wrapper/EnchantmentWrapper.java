package org.sandboxpowered.sandbox.fabric.util.wrapper;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.sandboxpowered.sandbox.api.enchantment.Enchantment;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

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

    @Override
    public Weight getWeight() {
        switch (enchantment.getWeight()) {
            case VERY_RARE:
                return Weight.VERY_RARE;
            case UNCOMMON:
                return Weight.UNCOMMON;
            case RARE:
                return Weight.RARE;
            default:
            case COMMON:
                return Weight.COMMON;
        }
    }

    @Override
    public void onTargetDamaged(LivingEntity livingEntity, Entity entity, int i) {
        enchantment.onTargetDamage((org.sandboxpowered.sandbox.api.entity.LivingEntity) WrappingUtil.convert(livingEntity), WrappingUtil.convert(entity), i);
    }

    @Override
    public void onUserDamaged(LivingEntity livingEntity, Entity entity, int i) {
        enchantment.onUserDamage((org.sandboxpowered.sandbox.api.entity.LivingEntity) WrappingUtil.convert(livingEntity), WrappingUtil.convert(entity), i);
    }
}
