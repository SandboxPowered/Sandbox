package com.hrznstudio.sandbox.mixin.impl.item;

import com.hrznstudio.sandbox.api.enchant.IEnchantment;
import com.hrznstudio.sandbox.api.item.IItem;
import com.hrznstudio.sandbox.api.item.ItemStack;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.item.ItemStack.class)
@Implements(@Interface(iface = ItemStack.class, prefix = "sbx$"))
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
}