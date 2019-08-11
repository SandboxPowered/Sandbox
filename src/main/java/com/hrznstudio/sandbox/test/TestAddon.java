package com.hrznstudio.sandbox.test;

import com.hrznstudio.sandbox.api.IAddon;
import com.hrznstudio.sandbox.client.SandboxClient;
import com.hrznstudio.sandbox.event.block.BlockEvent;
import com.hrznstudio.sandbox.event.client.ScreenEvent;
import com.hrznstudio.sandbox.event.mod.ModEvent;
import com.hrznstudio.sandbox.server.SandboxServer;
import com.hrznstudio.sandbox.util.Log;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.LanguageOptionsScreen;
import net.minecraft.util.registry.Registry;

public class TestAddon implements IAddon {

    @Override
    public void initServer(SandboxServer server) {
        server.getDispatcher().on(ModEvent.Init.class, ev -> {
            Log.debug(Thread.currentThread().getName());
        });
        server.getDispatcher().on(BlockEvent.Place.class, event -> {
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
        server.getDispatcher().on(BlockEvent.Break.class, event -> {
            Log.debug(Thread.currentThread().getName());
            Log.debug("Broke " + Registry.BLOCK.getId(event.getState().getBlock()));
            if (event.getState().getBlock() == Blocks.GRASS_BLOCK) {
                event.cancel();
            }
        });
    }

    @Override
    public void initClient(SandboxClient client) {
        client.getDispatcher().on(ScreenEvent.Open.class, event -> {
            if (event.getScreen() instanceof LanguageOptionsScreen)
                event.cancel();
        });
    }
}