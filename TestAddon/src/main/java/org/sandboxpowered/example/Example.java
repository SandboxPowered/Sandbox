package org.sandboxpowered.example;

import org.sandboxpowered.api.SandboxAPI;
import org.sandboxpowered.api.addon.Addon;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.block.Blocks;
import org.sandboxpowered.api.block.Material;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.entity.player.Hand;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.sandboxpowered.api.events.BlockEvents;
import org.sandboxpowered.api.item.BaseBlockItem;
import org.sandboxpowered.api.item.BaseItem;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.registry.Registrar;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.InteractionResult;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.World;

public class Example implements Addon {
    public static BlockEntity.Type<?> pipeEntityType;

    @Override
    public void init(SandboxAPI api) {
        api.getLog().info("Loading Example Addon");

        BlockEvents.INTERACT.subscribe((world, position, state, player, hand, stack, result) -> {
            if (result != InteractionResult.IGNORE)
                return result;
            if (!stack.getItem().getIdentity().getPath().contains("shovel"))
                return InteractionResult.IGNORE;
            if(!state.is(Blocks.DIRT) || !world.getBlockState(position.up()).isAir())
                return InteractionResult.IGNORE;
            if(world.isServer()) {
                world.setBlockState(position, Blocks.GRASS_PATH.get());
                stack.damage(1, player);
            }
            return InteractionResult.SUCCESS;
        });
    }

    @Override
    public void register(SandboxAPI api, Registrar registrar) {
        api.getLog().info("Registering Content");
        PipeBlock pipe = new PipeBlock(Block.Settings.builder(Material.METAL).build());
        pipeEntityType = BlockEntity.Type.of(() -> new PipeBlockEntity(pipeEntityType), pipe);
        registrar.register("pipe", pipe);
        registrar.register("pipe", pipeEntityType);
        registrar.register("pipe", new BaseBlockItem(pipe, new Item.Settings()));
        registrar.register("test", new BaseItem(new Item.Settings()));
        api.getLog().info("Finished Registration");
    }
}