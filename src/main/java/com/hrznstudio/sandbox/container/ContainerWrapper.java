package com.hrznstudio.sandbox.container;

import com.hrznstudio.sandbox.api.container.IContainer;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;

public class ContainerWrapper extends Container {
    private IContainer container;

    public ContainerWrapper(@Nullable ContainerType<?> containerType_1, int int_1, IContainer container) {
        super(containerType_1, int_1);
        this.container = container;
        container.getSlots().stream().map(SlotWrapper::new).forEach(this::addSlot);
    }

    @Override
    public boolean canUse(PlayerEntity var1) {
        return true;
    }
}