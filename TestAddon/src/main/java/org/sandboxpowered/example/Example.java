package org.sandboxpowered.example;

import org.sandboxpowered.api.SandboxAPI;
import org.sandboxpowered.api.addon.Addon;
import org.sandboxpowered.api.block.*;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.events.BlockEvents;
import org.sandboxpowered.api.item.BaseItem;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.registry.Registrar;
import org.sandboxpowered.api.resources.Resource;
import org.sandboxpowered.api.resources.ResourceConstants;
import org.sandboxpowered.api.resources.ResourceService;
import org.sandboxpowered.api.state.Properties;
import org.sandboxpowered.api.state.property.Property;
import org.sandboxpowered.api.tags.BlockTags;
import org.sandboxpowered.api.tags.Tag;
import org.sandboxpowered.api.util.Direction;
import org.sandboxpowered.api.util.InteractionResult;

import java.util.List;

public class Example implements Addon {
    public static BlockEntity.Type<?> pipeEntityType;

    @Override
    public void init(SandboxAPI api) {
        api.getLog().info("Loading Example Addon");

        BlockEvents.BREAK.subscribe((world, position, state, player, tool, cancellable) -> {
            if (state.is(Blocks.BEDROCK))
                cancellable.cancel();
        });
        BlockEvents.INTERACT.subscribe((world, position, state, player, hand, stack, result) -> {
            if (result != InteractionResult.IGNORE)
                return result;
            if (world.isClient())
                return InteractionResult.IGNORE;
            Block block = state.getBlock();
            if(block.isIn(BlockTags.DOORS)) {
                Property<Boolean> open = Properties.OPEN;
                Property<Direction> dir = Properties.HORIZONTAL_FACING;
            }

            if (block instanceof CropBlock) {
                CropBlock crop = (CropBlock) block;
                int age = crop.getAge(state);
                if (age >= crop.getMaxAge()) {
                    List<ItemStack> drops = block.getDrops(world, position, state);

                    boolean foundSeed = false;

                    for (ItemStack drop : drops) {
                        if (drop.isEmpty())
                            continue;

                        if (drop.getItem() == crop.getSeed()) {
                            drop.shrink(1);
                            foundSeed = true;
                            break;
                        }
                    }

                    if (foundSeed) {
                        if (world.isServer()) {
                            world.setBlockState(position, crop.stateForAge(0));
                            for (ItemStack drop : drops) {
                                world.spawnItem(position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, drop);
                            }
                        }
//                        player.swingHand(hand);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.IGNORE;
        });
    }

    public void registerMaterials(ResourceService service) {
        service.add(ResourceConstants.COAL, ResourceConstants.INGOT);
    }

    @Override
    public void register(SandboxAPI api, Registrar registrar) {
        registrar.useRegistrarService(ResourceService.class, this::registerMaterials);
        registrar.register("test", new BaseItem(new Item.Settings()));
        registrar.register("block", new BaseBlock(Block.Settings.builder(Material.STONE).build()));
        registrar.register("block_no_itemblock", new BaseBlock(Block.Settings.builder(Material.STONE).removeItemBlock().build()));
    }
}