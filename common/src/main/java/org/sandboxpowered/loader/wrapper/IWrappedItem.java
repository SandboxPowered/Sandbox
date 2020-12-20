package org.sandboxpowered.loader.wrapper;

import org.sandboxpowered.api.item.Item;

public interface IWrappedItem {
    Item getAsSandbox();

    net.minecraft.world.item.Item getAsVanilla();
}
