package com.hrznstudio.sandbox.test;

import com.hrznstudio.sandbox.api.SandboxAPI;
import com.hrznstudio.sandbox.api.addon.Addon;
import com.hrznstudio.sandbox.api.util.Side;

public class TestAddon implements Addon {
    @Override
    public void init(SandboxAPI api) {
        //Common Registration
        api.execute(Side.CLIENT, () -> {
            //Client Only Registration
        });
        api.execute(Side.SERVER, () -> {
            //Server Only Registration
        });
    }

//    @Override
//    public void initServer(SandboxServer server) {
//        server.getDispatcher().on(ModEvent.Init.class, ev -> {
//            Log.debug(Thread.currentThread().getName());
//        });
//        server.getDispatcher().on(BlockEvent.Place.class, event -> {
//            Log.debug(Thread.currentThread().getName());
//            Log.debug("Placed " + Registry.BLOCK.getId(event.getState().getBlock()));
//            NetworkManager.sendToAll(new TestPacket(event.getState().toString()));
//            if (event.getState().getBlock() == Blocks.GRASS_BLOCK) {
//                if (event.getContext().getWorld().getBlockState(event.getContext().getBlockPos().down()).getBlock() != Blocks.GOLD_BLOCK)
//                    event.cancel();
//            }
//            if (event.getState().getBlock() == Blocks.OAK_PLANKS) {
//                if (event.getContext().getWorld().getBlockState(event.getContext().getBlockPos().down()).getBlock() != Blocks.DIAMOND_BLOCK)
//                    event.cancel();
//            }
//        });
//        server.getDispatcher().on(BlockEvent.Break.class, event -> {
//            Log.debug(Thread.currentThread().getName());
//            Log.debug("Broke " + Registry.BLOCK.getId(event.getState().getBlock()));
//            if (event.getState().getBlock() == Blocks.GRASS_BLOCK) {
//                event.cancel();
//            }
//        });
//    }
//
//    @Override
//    public void initClient(SandboxClient client) {
//        client.getDispatcher().on(ScreenEvent.Open.class, event -> {
//            if (event.getScreen() instanceof LanguageOptionsScreen)
//                event.cancel();
//        });
//    }
}