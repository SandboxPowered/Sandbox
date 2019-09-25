package org.sandboxpowered.sandbox.fabric.container;

import org.sandboxpowered.sandbox.api.container.Slot;
import org.sandboxpowered.sandbox.api.entity.player.PlayerEntity;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SlotWrapper extends net.minecraft.container.Slot {
    private final Slot slot;

    public SlotWrapper(Slot slot) {
        super(null, 0, slot.getXPosition(), slot.getYPosition());
        this.slot = slot;
    }

    @Override
    public boolean canInsert(ItemStack itemStack_1) {
        return slot.canInsert(WrappingUtil.cast(itemStack_1, org.sandboxpowered.sandbox.api.item.ItemStack.class));
    }

    @Override
    public boolean canTakeItems(net.minecraft.entity.player.PlayerEntity playerEntity_1) {
        return slot.canExtract((PlayerEntity) playerEntity_1);
    }

    @Override
    public int getMaxStackAmount() {
        return slot.getMaxCount();
    }

    @Override
    public boolean doDrawHoveringEffect() {
        return slot.drawHovering();
    }

    @Override
    public ItemStack getStack() {
        return WrappingUtil.cast(slot.getStack(), ItemStack.class);
    }

    @Override
    public void setStack(ItemStack itemStack_1) {
        slot.setStack(WrappingUtil.cast(itemStack_1, org.sandboxpowered.sandbox.api.item.ItemStack.class));
    }

    @Override
    public boolean hasStack() {
        return slot.hasStack();
    }

    @Override
    public ItemStack takeStack(int int_1) {
        return WrappingUtil.cast(slot.takeStack(int_1), ItemStack.class);
    }

    @Nullable
    @Override
    public String getBackgroundSprite() {
        return slot.getBackgroundSprite().orElse(null);
    }
}