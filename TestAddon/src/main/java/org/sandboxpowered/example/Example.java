package org.sandboxpowered.example;

import org.sandboxpowered.api.SandboxAPI;
import org.sandboxpowered.api.addon.Addon;
import org.sandboxpowered.api.block.Blocks;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.events.BlockEvents;
import org.sandboxpowered.api.item.BaseItem;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.registry.Registrar;
import org.sandboxpowered.api.util.InteractionResult;

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
        registrar.register("test", new BaseItem(new Item.Settings()));
    }
}