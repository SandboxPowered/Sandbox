package com.hrznstudio.sandbox.test;

import com.hrznstudio.sandbox.api.SandboxAPI;
import com.hrznstudio.sandbox.api.addon.Addon;
import com.hrznstudio.sandbox.api.block.Block;
import com.hrznstudio.sandbox.api.block.state.BlockState;
import com.hrznstudio.sandbox.api.entity.Entity;
import com.hrznstudio.sandbox.api.entity.player.Hand;
import com.hrznstudio.sandbox.api.event.RegistryEvent;
import com.hrznstudio.sandbox.api.util.Activation;
import com.hrznstudio.sandbox.api.util.Direction;
import com.hrznstudio.sandbox.api.util.Identity;
import com.hrznstudio.sandbox.api.util.Side;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.util.math.Vec3f;
import com.hrznstudio.sandbox.api.world.World;
import com.hrznstudio.sandbox.util.Log;

public class TestAddon implements Addon {
    private void registerBlocks(RegistryEvent<Block> event) {
        event.getRegistry().register(Identity.of("test", "test"), new Block() {
            @Override
            public Activation onBlockUsed(World world, Position pos, BlockState state, Entity player, Hand hand, Direction side, Vec3f hit) {
                return Activation.SUCCESS;
            }
        });
    }

    @Override
    public void init(SandboxAPI api) {
        //Common Registration
        api.onGeneric(Block.class, this::registerBlocks);
        Log.debug("Loading Test Addon");
        api.execute(Side.CLIENT, () -> {
            //Client Only Registration
            Log.debug("Loading Test Addon Client");
        });
        api.execute(Side.SERVER, () -> {
            //Server Only Registration
            Log.debug("Loading Test Addon Server");
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