package org.sandboxpowered.sandbox.fabric.impl.event;

import org.sandboxpowered.api.events.args.ItemArgs;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.eventhandler.CancellableEventArgs;

public class ItemArgsImpl extends CancellableEventArgs implements ItemArgs {
    private final ItemStack stack;

    public ItemArgsImpl(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public ItemStack getStack() {
        return stack;
    }
}
