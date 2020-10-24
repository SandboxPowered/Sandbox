package org.sandboxpowered.sandbox.fabric.util;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import org.sandboxpowered.sandbox.fabric.Sandbox;

public class RegistryUtil {
    private RegistryUtil() {
    }

    public static void doOnSet(Object object) {
        if (object instanceof Item && Sandbox.SANDBOX.getSide().isClient()) {
            MinecraftClient.getInstance().getItemRenderer().getModels().putModel((Item) object, new ModelIdentifier(Registry.ITEM.getId((Item) object), "inventory"));
        }
        if (object instanceof BlockItem) {
            ((BlockItem) object).appendBlocks(Item.BLOCK_ITEMS, (BlockItem) object);
        }
        if (object instanceof Block) {
            ((Block) object).getStateManager().getStates().forEach(Block.STATE_IDS::add);
            //TODO: Also need to reset the state ids
        }
    }
}
