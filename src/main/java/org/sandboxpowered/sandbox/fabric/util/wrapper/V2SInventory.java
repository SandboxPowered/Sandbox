package org.sandboxpowered.sandbox.fabric.util.wrapper;

import org.sandboxpowered.sandbox.api.component.Inventory;
import org.sandboxpowered.sandbox.api.item.ItemStack;
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
        ItemStack original = get(slot).copy();
        if (original.isEqualTo(stack)) {
            int originalCount = original.getCount();
            original.setCount(Math.min(getMaxStackSize(slot), originalCount + stack.getCount()));
            if (!simulate) {
                inventory.setInvStack(slot, WrappingUtil.cast(original, net.minecraft.item.ItemStack.class));
            }
            return stack.copy().setCount(original.getCount() - originalCount);
        } else if (original.isEmpty() && inventory.isValidInvStack(slot, WrappingUtil.cast(stack, net.minecraft.item.ItemStack.class))) {
            ItemStack toInsert = stack.copy();
            toInsert.setCount(Math.min(toInsert.getCount(), getMaxStackSize(slot)));
            if (!simulate) {
                inventory.setInvStack(slot, WrappingUtil.cast(toInsert, net.minecraft.item.ItemStack.class));
            }
            return stack.copy().setCount(stack.getCount() - toInsert.getCount());
        }
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.setInvStack(slot, WrappingUtil.cast(stack, net.minecraft.item.ItemStack.class));
    }
}