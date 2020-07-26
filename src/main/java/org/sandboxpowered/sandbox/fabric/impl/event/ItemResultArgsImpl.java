package org.sandboxpowered.sandbox.fabric.impl.event;

import org.sandboxpowered.api.events.args.ItemResultArgs;
import org.sandboxpowered.api.item.ItemStack;

public class ItemResultArgsImpl extends ResultArgsImpl implements ItemResultArgs {
    private final ItemStack stack;

    public ItemResultArgsImpl(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public ItemStack getStack() {
        return stack;
    }
}
