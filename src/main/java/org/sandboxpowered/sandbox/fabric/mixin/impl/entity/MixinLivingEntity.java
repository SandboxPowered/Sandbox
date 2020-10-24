package org.sandboxpowered.sandbox.fabric.mixin.impl.entity;

import net.minecraft.entity.EquipmentSlot;
import org.sandboxpowered.api.entity.LivingEntity;
import org.sandboxpowered.api.entity.player.Hand;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.entity.LivingEntity.class)
@Implements(@Interface(iface = LivingEntity.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
@SuppressWarnings({"java:S100","java:S1610"})
public abstract class MixinLivingEntity {
    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract void setHealth(float hp);

    @Shadow
    public abstract net.minecraft.item.ItemStack getEquippedStack(EquipmentSlot equipmentSlot);

    @Shadow
    public abstract void equipStack(EquipmentSlot equipmentSlot, net.minecraft.item.ItemStack itemStack);

    @Shadow
    public abstract void swingHand(net.minecraft.util.Hand hand, boolean bl);

    @Shadow
    public abstract net.minecraft.item.ItemStack getStackInHand(net.minecraft.util.Hand hand);

    @Shadow
    public abstract void setStackInHand(net.minecraft.util.Hand hand, net.minecraft.item.ItemStack itemStack);

    public void sbx$setHealth(float health) {
        this.setHealth(health);
    }

    public float sbx$getHealth() {
        return this.getHealth();
    }

    public ItemStack sbx$getEquipped(LivingEntity.EquipmentSlot slot) {
        return WrappingUtil.convert(getEquippedStack(WrappingUtil.convert(slot)));
    }

    public void sbx$equip(LivingEntity.EquipmentSlot slot, ItemStack stack) {
        equipStack(WrappingUtil.convert(slot), WrappingUtil.convert(stack));
    }

    public void sbx$swing(Hand hand, boolean updateSelf) {
        swingHand(
                WrappingUtil.convert(hand),
                updateSelf
        );
    }

    public void sbx$setHeld(Hand hand, ItemStack stack) {
        setStackInHand(WrappingUtil.convert(hand), WrappingUtil.convert(stack));
    }

    public ItemStack sbx$getHeld(Hand hand) {
        return WrappingUtil.convert(getStackInHand(WrappingUtil.convert(hand)));
    }

}