package org.sandboxpowered.sandbox.fabric.impl.event;

import org.sandboxpowered.api.events.args.ItemArgs;
import org.sandboxpowered.api.events.args.ItemModifiableArgs;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.eventhandler.CancellableEventArgs;

public class ItemModifiableArgsImpl extends CancellableEventArgs implements ItemModifiableArgs {
    private ItemStack stack;

    public ItemModifiableArgsImpl(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void setStack(ItemStack stack) {
        this.stack = stack;
    }
}