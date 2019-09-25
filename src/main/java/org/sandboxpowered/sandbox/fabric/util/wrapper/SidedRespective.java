package org.sandboxpowered.sandbox.fabric.util.wrapper;

import net.minecraft.inventory.SidedInventory;
import org.apache.commons.lang3.ArrayUtils;
import org.sandboxpowered.sandbox.api.item.ItemStack;
import org.sandboxpowered.sandbox.api.util.Direction;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

import java.util.function.Predicate;

public class SidedRespective extends V2SInventory {
    private final Direction direction;
    private final SidedInventory inventory;

    public SidedRespective(SidedInventory inventory, Direction direction) {
        super(inventory);
        this.direction = direction;
        this.inventory = inventory;
    }

    @Override
    public int getMaxStackSize(int slot) {
        if (ArrayUtils.contains(inventory.getInvAvailableSlots(WrappingUtil.convert(direction)), slot))
            return super.getMaxStackSize(slot);
        return 0;
    }

    @Override
    public ItemStack get(int slot) {
        if (ArrayUtils.contains(inventory.getInvAvailableSlots(WrappingUtil.convert(direction)), slot))
            return super.get(slot);
        return ItemStack.empty();
    }

    @Override
    public ItemStack extract(int slot, Predicate<ItemStack> itemFilter, int amount, boolean simulate) {
        if (ArrayUtils.contains(inventory.getInvAvailableSlots(WrappingUtil.convert(direction)), slot) && inventory.canExtractInvStack(slot, WrappingUtil.cast(get(slot), net.minecraft.item.ItemStack.class), WrappingUtil.convert(direction)))
            return super.extract(slot, itemFilter, amount, simulate);
        return ItemStack.empty();
    }

    @Override
    public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
        if (ArrayUtils.contains(inventory.getInvAvailableSlots(WrappingUtil.convert(direction)), slot) && inventory.canInsertInvStack(slot, WrappingUtil.cast(stack, net.minecraft.item.ItemStack.class), WrappingUtil.convert(direction)))
            return super.insert(slot, stack, simulate);
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (ArrayUtils.contains(inventory.getInvAvailableSlots(WrappingUtil.convert(direction)), slot))
            super.setStack(slot, stack);
    }
}