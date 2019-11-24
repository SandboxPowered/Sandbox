package org.sandboxpowered.sandbox.fabric.container;

import net.minecraft.container.ContainerType;
import net.minecraft.entity.player.PlayerEntity;
import org.sandboxpowered.sandbox.api.container.Container;

import javax.annotation.Nullable;

public class ContainerWrapper extends net.minecraft.container.Container {
    private Container container;

    public ContainerWrapper(@Nullable ContainerType<?> containerType_1, int int_1, Container container) {
        super(containerType_1, int_1);
        this.container = container;
        this.container.getSlots().stream().map(SlotWrapper::new).forEach(this::addSlot);
    }

    @Override
    public boolean canUse(PlayerEntity var1) {
        return true;
    }
}