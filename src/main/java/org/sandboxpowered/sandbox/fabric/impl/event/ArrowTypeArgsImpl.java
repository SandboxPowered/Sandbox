package org.sandboxpowered.sandbox.fabric.impl.event;

import org.sandboxpowered.api.events.args.ArrowTypeArgs;
import org.sandboxpowered.api.events.args.ItemArgs;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.eventhandler.CancellableEventArgs;

public class ArrowTypeArgsImpl implements ArrowTypeArgs {
    private final ItemStack weapon;
    private ItemStack arrow;

    public ArrowTypeArgsImpl(ItemStack weapon, ItemStack arrow) {
        this.weapon = weapon;
        this.arrow = arrow;
    }

    @Override
    public ItemStack getWeapon() {
        return weapon;
    }

    @Override
    public ItemStack getArrow() {
        return arrow;
    }

    @Override
    public void setArrow(ItemStack stack) {
        this.arrow=stack;
    }
}
