package org.sandboxpowered.loader.inject.factory;

import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.util.nbt.CompoundTag;
import org.sandboxpowered.api.util.nbt.ReadableCompoundTag;
import org.sandboxpowered.loader.Wrappers;

public class ItemStackFactory implements ItemStack.Factory {
    @Override
    public ItemStack create(Item item, int count, @Nullable CompoundTag nbt) {
        if (item == null || count <= 0)
            return Wrappers.ITEMSTACK.toSandbox(net.minecraft.world.item.ItemStack.EMPTY);
        return Wrappers.ITEMSTACK.toSandbox(new net.minecraft.world.item.ItemStack(
                Wrappers.ITEM.toVanilla(item),
                count
        ));
    }

    @Override
    public ItemStack fromTag(ReadableCompoundTag tag) {
        return null;
    }
}
