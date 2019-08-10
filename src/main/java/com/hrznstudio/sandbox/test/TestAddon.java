package com.hrznstudio.sandbox.test;

import com.hrznstudio.sandbox.event.EventDispatcher;
import com.hrznstudio.sandbox.event.block.BlockEvent;
import com.hrznstudio.sandbox.event.client.ScreenEvent;
import com.hrznstudio.sandbox.event.mod.ModEvent;
import com.hrznstudio.sandbox.util.Log;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.LanguageOptionsScreen;
import net.minecraft.util.registry.Registry;

public class TestAddon {
    public TestAddon() {
        EventDispatcher.getServerDispatcher()
                .on(ModEvent.Init.class, ev -> {
                    Log.debug(Thread.currentThread().getName());
                });
        EventDispatcher.getClientDispatcher()
                .on(ScreenEvent.Open.class, event -> {
                    if (event.getScreen() instanceof LanguageOptionsScreen)
                        event.cancel();
                });
        EventDispatcher.getServerDispatcher()
                .on(BlockEvent.PlaceEvent.class, event -> {
                    Log.debug(Thread.currentThread().getName());
                    Log.debug("Placed " + Registry.BLOCK.getId(event.getState().getBlock()));
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