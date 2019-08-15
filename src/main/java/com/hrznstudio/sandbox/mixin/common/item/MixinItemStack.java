package com.hrznstudio.sandbox.mixin.common.item;

import com.hrznstudio.sandbox.api.item.Item;
import com.hrznstudio.sandbox.api.item.Stack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.*;

@Mixin(ItemStack.class)
@Implements(@Interface(iface = Stack.class, prefix = "sbx$"))
@Unique
public abstract class MixinItemStack {

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract net.minecraft.item.Item getItem();

    @Shadow
    public abstract int getCount();

    @Shadow
    public abstract void decrement(int int_1);

    @Shadow
    public abstract void increment(int int_1);

    @Shadow
    public abstract void setCount(int int_1);

    @Shadow
    public abstract net.minecraft.util.Rarity getRarity();

    public boolean sbx$isEmpty() {
        return this.isEmpty();
    }


    public Item sbx$getItem() {
        return (Item) this.getItem();
    }


    public int sbx$getCount() {
        return this.getCount();
    }


    public Stack sbx$shrink(int amount) {
        this.decrement(amount);
        return (Stack) this;
    }


    public Stack sbx$grow(int amount) {
        this.increment(amount);
        return (Stack) this;
    }


    public Stack sbx$setCount(int amount) {
        this.setCount(amount);
        return (Stack) this;
    }
}