package org.sandboxpowered.sandbox.fabric.util.wrapper;

import org.sandboxpowered.api.component.Inventory;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

import java.util.function.Predicate;

public class V2SInventory implements Inventory {
    protected final net.minecraft.inventory.Inventory inventory;

    public V2SInventory(net.minecraft.inventory.Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public boolean isEmpty() {
        return inventory.isInvEmpty();
    }

    @Override
    public int getSize() {
        return inventory.getInvSize();
    }

    @Override
    public int getMaxStackSize(int slot) {
        return Math.min(inventory.getInvMaxStackAmount(), get(slot).getMaxCount());
    }

    @Override
    public ItemStack get(int slot) {
        return WrappingUtil.cast(inventory.getInvStack(slot), ItemStack.class);
    }

    @Override
    public ItemStack extract(int slot, Predicate<ItemStack> itemFilter, int amount, boolean simulate) {
        ItemStack out = get(slot).copy();
        out.setCount(Math.min(out.getCount(), amount));

        if (!itemFilter.test(out))
            return ItemStack.empty();

        if (!simulate) {
            out = WrappingUtil.cast(inventory.takeInvStack(slot, amount), ItemStack.class);
        }

        return out;
    }

    @Override
    public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
        ItemStack in = get(slot);
        int max = getMaxStackSize(slot);
        if (in.isEmpty()) {
            ItemStack coming = stack.copy();
            if (coming.getCount() > max) {
                coming.setCount(max);
            }
            if (!simulate)
                setStack(slot, coming);
            return stack.copy().shrink(coming.getCount());
        } else {
            int count = in.getCount();
            if (count >= max || !in.isEqualTo(stack) || !in.areTagsEqual(stack))
                return stack;
            ItemStack coming = stack.copy();
            if (count + coming.getCount() > max) {
                coming.setCount(max - count);
            }
            if (!simulate)
                setStack(slot, in.copy().grow(coming.getCount()));
            return stack.copy().shrink(coming.getCount());
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.setInvStack(slot, WrappingUtil.cast(stack, net.minecraft.item.ItemStack.class));
    }
}