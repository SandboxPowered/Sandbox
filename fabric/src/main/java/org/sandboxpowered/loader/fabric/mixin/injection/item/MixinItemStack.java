package org.sandboxpowered.loader.fabric.mixin.injection.item;

import net.minecraft.core.Registry;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.enchantment.Enchantment;
import org.sandboxpowered.api.entity.LivingEntity;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.util.nbt.CompoundTag;
import org.sandboxpowered.loader.Wrappers;
import org.spongepowered.asm.mixin.*;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mixin(ItemStack.class)
@Implements(@Interface(iface = org.sandboxpowered.api.item.ItemStack.class, prefix = "stack$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinItemStack {
    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract ItemStack copy();

    @Shadow
    public abstract int getCount();

    @Shadow
    public abstract void setCount(int p_190920_1_);

    @Shadow
    public abstract int getMaxDamage();

    @Shadow
    public abstract int getDamageValue();

    @Shadow
    public abstract void setDamageValue(int p_196085_1_);

    @Shadow
    public abstract boolean isDamaged();

    @Shadow
    public abstract boolean isDamageableItem();

    @Shadow
    public abstract int getMaxStackSize();

    @Shadow
    public abstract void shrink(int p_190918_1_);

    @Shadow
    public abstract void grow(int p_190917_1_);

    @Shadow
    public abstract net.minecraft.world.item.Item getItem();

    @Shadow
    public abstract ListTag getEnchantmentTags();

    @Shadow
    public abstract <T extends net.minecraft.world.entity.LivingEntity> void hurtAndBreak(int i, T livingEntity, Consumer<T> consumer);

    @NotNull
    private org.sandboxpowered.api.item.ItemStack asSandbox(ItemStack stack) {
        return Wrappers.ITEMSTACK.toSandbox(stack);
    }

    @NotNull
    private org.sandboxpowered.api.item.ItemStack asSandbox() {
        return (org.sandboxpowered.api.item.ItemStack) this;
    }

    @NotNull
    private ItemStack asVanilla() {
        return (ItemStack) (Object) this;
    }

    @NotNull
    private ItemStack asVanilla(org.sandboxpowered.api.item.ItemStack stack) {
        return (ItemStack) (Object) stack;
    }

    public boolean stack$isEmpty() {
        return isEmpty();
    }

    public Item stack$getItem() {
        return Wrappers.ITEM.toSandbox(getItem());
    }

    public int stack$getCount() {
        return getCount();
    }

    public org.sandboxpowered.api.item.ItemStack stack$setCount(int amount) {
        setCount(amount);
        return asSandbox();
    }

    public org.sandboxpowered.api.item.ItemStack stack$copy() {
        return asSandbox(copy());
    }

    public org.sandboxpowered.api.item.ItemStack stack$shrink(int amount) {
        shrink(amount);
        return asSandbox();
    }

    public org.sandboxpowered.api.item.ItemStack stack$grow(int amount) {
        grow(amount);
        return asSandbox();
    }

    public boolean stack$has(Enchantment enchantment) {
        return stack$getLevel(enchantment) > 0;
    }

    public int stack$getLevel(Enchantment enchantment) {
        return EnchantmentHelper.getItemEnchantmentLevel(Wrappers.ENCHANTMENT.toVanilla(enchantment), asVanilla());
    }

    public Set<Enchantment> stack$getEnchantments() {
        if (stack$isEmpty())
            return Collections.emptySet();
        ListTag enchantments = getEnchantmentTags();
        return IntStream.range(0, enchantments.size())
                .mapToObj(enchantments::getCompound)
                .map(tag -> tag.getString("id"))
                .map(ResourceLocation::tryParse)
                .filter(Objects::nonNull)
                .map(Registry.ENCHANTMENT::get)
                .filter(Objects::nonNull)
                .map(enchantment -> Wrappers.ENCHANTMENT.toSandbox(enchantment))
                .collect(Collectors.toSet());
    }

    public boolean stack$hasTag() {
        throw new NotImplementedException("TODO");
    }

    public @Nullable CompoundTag stack$getTag() {
        throw new NotImplementedException("TODO");
    }

    public void stack$setTag(CompoundTag tag) {
        throw new NotImplementedException("TODO");
    }

    public CompoundTag stack$getOrCreateTag() {
        throw new NotImplementedException("TODO");
    }

    public @Nullable CompoundTag stack$getChildTag(String key) {
        throw new NotImplementedException("TODO");
    }

    public CompoundTag stack$getOrCreateChildTag(String key) {
        throw new NotImplementedException("TODO");
    }

    public CompoundTag stack$asTag() {
        throw new NotImplementedException("TODO");
    }

    public int stack$getMaxCount() {
        return getMaxStackSize();
    }

    public boolean stack$isEqualTo(@NotNull org.sandboxpowered.api.item.ItemStack stack) {
        return ItemStack.isSame(asVanilla(), asVanilla(stack));
    }

    public boolean stack$isEqualToIgnoreDurability(@NotNull org.sandboxpowered.api.item.ItemStack stack) {
        return ItemStack.isSameIgnoreDurability(asVanilla(), asVanilla(stack));
    }

    public boolean stack$areTagsEqual(@NotNull org.sandboxpowered.api.item.ItemStack stack) {
        return ItemStack.tagMatches(asVanilla(), asVanilla(stack));
    }

    public boolean stack$isDamaged() {
        return isDamaged();
    }

    public boolean stack$isDamageable() {
        return isDamageableItem();
    }

    public int stack$getMaxDamage() {
        return getMaxDamage();
    }

    public int stack$getDamage() {
        return getDamageValue();
    }

    public void stack$damage(int damage, LivingEntity entity) {
        hurtAndBreak(damage, Wrappers.LIVING_ENTITY.toVanilla(entity), livingEntity -> livingEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
    }
}