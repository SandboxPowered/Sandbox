package org.sandboxpowered.sandbox.fabric.util.wrapper;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.sandboxpowered.api.enchantment.Enchantment;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

public class EnchantmentWrapper extends net.minecraft.enchantment.Enchantment implements SandboxInternal.IEnchantmentWrapper {
    private final Enchantment enchantment;

    public EnchantmentWrapper(Enchantment enchantment) {
        super(WrappingUtil.convert(enchantment.getRarity()), EnchantmentTarget.WEARABLE, EquipmentSlot.values()); //TODO: Allow sandbox enchants to specify these values
        this.enchantment = enchantment;
    }

    @Override
    public Enchantment getSandboxEnchantment() {
        return enchantment;
    }

    @Override
    public int getMinLevel() {
        return enchantment.getMinimumLevel();
    }

    @Override
    public int getMaxLevel() {
        return enchantment.getMaximumLevel();
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return enchantment.isAcceptableItem(WrappingUtil.cast(stack, org.sandboxpowered.api.item.ItemStack.class));
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
    public Rarity getRarity() {
        return WrappingUtil.convert(enchantment.getRarity());
    }

    @Override
    public void onTargetDamaged(LivingEntity livingEntity, Entity entity, int i) {
        enchantment.onTargetDamage((org.sandboxpowered.api.entity.LivingEntity) WrappingUtil.convert(livingEntity), WrappingUtil.convert(entity), i);
    }

    @Override
    public void onUserDamaged(LivingEntity livingEntity, Entity entity, int i) {
        enchantment.onUserDamage((org.sandboxpowered.api.entity.LivingEntity) WrappingUtil.convert(livingEntity), WrappingUtil.convert(entity), i);
    }
}
