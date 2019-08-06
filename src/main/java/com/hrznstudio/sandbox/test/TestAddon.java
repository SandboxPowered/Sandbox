package com.hrznstudio.sandbox.test;

import com.hrznstudio.sandbox.event.CancellableEvent;
import com.hrznstudio.sandbox.event.EventDispatcher;
import com.hrznstudio.sandbox.event.block.BlockEvent;
import com.hrznstudio.sandbox.event.client.OpenScreenEvent;
import com.hrznstudio.sandbox.event.mod.ModInitEvent;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.LanguageOptionsScreen;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TestAddon {
    public TestAddon() {
        EventDispatcher.getServerDispatcher()
                .on(ModInitEvent.class)
                .subscribe(ev -> {
                    Registry.ITEM.add(new Identifier("test", "test"), new Item(new Item.Settings()));
                    Registry.ITEM.add(new Identifier("test", "test2"), new Item(new Item.Settings()));
                    Registry.ITEM.add(new Identifier("test", "test3"), new Item(new Item.Settings()));
                });
        EventDispatcher.getClientDispatcher()
                .on(OpenScreenEvent.class)
                .filter(event -> event.getScreen() instanceof LanguageOptionsScreen)
                .subscribe(CancellableEvent::cancel);

        EventDispatcher.getServerDispatcher()
                .on(BlockEvent.PlaceEvent.class)
                .subscribe(event -> {
                    System.out.println("Placed " + Registry.BLOCK.getId(event.getState().getBlock()));
                    if (event.getState().getBlock() == Blocks.GRASS_BLOCK) {
                        if (event.getContext().getWorld().getBlockState(event.getContext().getBlockPos().down()).getBlock() != Blocks.GOLD_BLOCK)
                            event.cancel();
                    }
                    if (event.getState().getBlock() == Blocks.OAK_PLANKS) {
                        if (event.getContext().getWorld().getBlockState(event.getContext().getBlockPos().down()).getBlock() != Blocks.DIAMOND_BLOCK)
                            event.cancel();
                    }
                });
    }
}