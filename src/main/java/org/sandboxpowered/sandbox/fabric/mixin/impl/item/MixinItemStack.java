package org.sandboxpowered.sandbox.fabric.mixin.impl.item;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.enchantment.Enchantment;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.util.nbt.CompoundTag;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("EqualsBetweenInconvertibleTypes")
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

    @Shadow
    public abstract boolean isDamaged();

    @Shadow
    public abstract boolean isDamageable();

    @Shadow
    public abstract int getMaxDamage();

    @Shadow
    public abstract int getDamage();

    @Shadow
    public abstract ListTag getEnchantments();

    public boolean sbx$isEmpty() {
        return this.isEmpty();
    }

    public Item sbx$getItem() {
        return WrappingUtil.convert(getItem());
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

    public int sbx$getLevel(Enchantment enchantment) {
        return EnchantmentHelper.getLevel(WrappingUtil.convert(enchantment), (net.minecraft.item.ItemStack) (Object) this);
    }

    @Inject(method = "isEffectiveOn", at = @At("HEAD"), cancellable = true)
    public void isEffectiveOn(BlockState state, CallbackInfoReturnable<Boolean> info) {
        if (getItem() instanceof SandboxInternal.IItemWrapper) {
            info.setReturnValue(((SandboxInternal.IItemWrapper) getItem()).getItem().isEffectiveOn(WrappingUtil.convert((net.minecraft.item.ItemStack) (Object) this), WrappingUtil.convert(state)));
        }
    }

    public Set<Enchantment> sbx$getEnchantments() {
        if (isEmpty())
            return Collections.emptySet();
        Set<Enchantment> set = new HashSet<>();
        ListTag listTag = getEnchantments();
        for (int i = 0; i < listTag.size(); ++i) {
            net.minecraft.nbt.CompoundTag tag = listTag.getCompound(i);
            String string = tag.getString("id");
            Registry.ENCHANTMENT.getOrEmpty(Identifier.tryParse(string)).ifPresent((enchantment) -> {
                set.add(WrappingUtil.convert(enchantment));
            });
        }
        return set;
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
        return WrappingUtil.convert(copy());
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
        } else if ((sbx$isEmpty() || stack.isEmpty()) || (!hasTag() && stack.hasTag())) {
            return false;
        }
        return (!sbx$hasTag() && !stack.hasTag()) || Objects.equals(getTag(), stack.getTag());
    }

    public CompoundTag sbx$asTag() {
        return (CompoundTag) toTag(new net.minecraft.nbt.CompoundTag());
    }

    public boolean sbx$isDamaged() {
        return isDamaged();
    }

    public boolean sbx$isDamageable() {
        return isDamageable();
    }

    public int sbx$getMaxDamage() {
        return getMaxDamage();
    }

    public int sbx$getDamage() {
        return getDamage();
    }
}