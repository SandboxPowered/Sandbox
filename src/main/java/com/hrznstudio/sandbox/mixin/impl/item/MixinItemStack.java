package com.hrznstudio.sandbox.mixin.impl.item;

import com.hrznstudio.sandbox.api.enchant.IEnchantment;
import com.hrznstudio.sandbox.api.item.IItem;
import com.hrznstudio.sandbox.api.item.ItemStack;
import com.hrznstudio.sandbox.api.util.nbt.CompoundTag;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;

@Mixin(net.minecraft.item.ItemStack.class)
@Implements(@Interface(iface = ItemStack.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinItemStack {

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract net.minecraft.item.Item getItem();

    @Shadow
    public abstract int getCount();

    @Shadow
    public abstract void setCount(int int_1);

    @Shadow
    public abstract void decrement(int int_1);

    @Shadow
    public abstract void increment(int int_1);

    @Shadow
    public abstract net.minecraft.util.Rarity getRarity();

    @Shadow
    public abstract boolean hasTag();

    @Shadow
    @Nullable
    public abstract net.minecraft.nbt.CompoundTag getTag();

    @Shadow
    public abstract void setTag(@Nullable net.minecraft.nbt.CompoundTag compoundTag_1);

    @Shadow
    public abstract net.minecraft.nbt.CompoundTag getOrCreateTag();

    @Shadow
    @Nullable
    public abstract net.minecraft.nbt.CompoundTag getSubTag(String string_1);

    @Shadow
    public abstract net.minecraft.nbt.CompoundTag getOrCreateSubTag(String string_1);

    @Shadow
    public abstract int getMaxCount();

    @Shadow
    public abstract boolean isItemEqual(net.minecraft.item.ItemStack itemStack_1);

    @Shadow
    public abstract net.minecraft.item.ItemStack copy();

    @Shadow
    public abstract boolean isItemEqualIgnoreDamage(net.minecraft.item.ItemStack itemStack_1);

    @Shadow
    public abstract net.minecraft.nbt.CompoundTag toTag(net.minecraft.nbt.CompoundTag compoundTag_1);

    public boolean sbx$isEmpty() {
        return this.isEmpty();
    }

    public IItem sbx$getItem() {
        return (IItem) this.getItem();
    }

    public int sbx$getCount() {
        return this.getCount();
    }

    public ItemStack sbx$shrink(int amount) {
        this.decrement(amount);
        return (ItemStack) this;
    }

    public ItemStack sbx$grow(int amount) {
        this.increment(amount);
        return (ItemStack) this;
    }

    public ItemStack sbx$setCount(int amount) {
        this.setCount(amount);
        return (ItemStack) this;
    }

    public int sbx$getLevel(IEnchantment enchantment) {
        return EnchantmentHelper.getLevel(WrappingUtil.convert(enchantment), (net.minecraft.item.ItemStack) (Object) this);
    }

    public boolean sbx$hasTag() {
        return hasTag();
    }

    public CompoundTag sbx$getTag() {
        return (CompoundTag) getTag();
    }

    public void sbx$setTag(CompoundTag tag) {
        setTag((net.minecraft.nbt.CompoundTag) tag);
    }

    public CompoundTag sbx$getChildTag(String key) {
        return (CompoundTag) getSubTag(key);
    }

    public CompoundTag sbx$getOrCreateChildTag(String key) {
        return (CompoundTag) getOrCreateSubTag(key);
    }

    public CompoundTag sbx$getOrCreateTag() {
        return (CompoundTag) getOrCreateTag();
    }

    public int sbx$getMaxCount() {
        return getMaxCount();
    }

    public ItemStack sbx$copy() {
        return WrappingUtil.cast(copy(), ItemStack.class);
    }

    public boolean sbx$isEqualTo(ItemStack stack) {
        if (this == stack) {
            return true;
        } else {
            return (!this.sbx$isEmpty() && !stack.isEmpty()) && isItemEqual(WrappingUtil.cast(stack, net.minecraft.item.ItemStack.class));
        }
    }

    public boolean sbx$isEqualToIgnoreDurability(ItemStack stack) {
        if (this == stack) {
            return true;
        } else {
            return (!this.sbx$isEmpty() && !stack.isEmpty()) && isItemEqualIgnoreDamage(WrappingUtil.cast(stack, net.minecraft.item.ItemStack.class));
        }
    }

    public boolean sbx$areTagsEqual(ItemStack stack) {
        if (this == stack) {
            return true;
        } else if ((sbx$isEmpty() || stack.isEmpty()) || (!hasTag() || stack.hasTag())) {
            return false;
        }
        return getTag().equals(stack.getTag());
    }

    public CompoundTag sbx$asTag() {
        return (CompoundTag) toTag(new net.minecraft.nbt.CompoundTag());
    }
}