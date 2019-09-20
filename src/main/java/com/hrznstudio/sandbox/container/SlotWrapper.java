package com.hrznstudio.sandbox.container;

import com.hrznstudio.sandbox.api.container.ISlot;
import com.hrznstudio.sandbox.api.entity.player.Player;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SlotWrapper extends Slot {
    private final ISlot slot;

    public SlotWrapper(ISlot slot) {
        super(null, 0, slot.getXPosition(), slot.getYPosition());
        this.slot = slot;
    }

    @Override
    public boolean canInsert(ItemStack itemStack_1) {
        return slot.canInsert(WrappingUtil.cast(itemStack_1, com.hrznstudio.sandbox.api.item.ItemStack.class));
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity_1) {
        return slot.canExtract((Player) playerEntity_1);
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
        slot.setStack(WrappingUtil.cast(itemStack_1, com.hrznstudio.sandbox.api.item.ItemStack.class));
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