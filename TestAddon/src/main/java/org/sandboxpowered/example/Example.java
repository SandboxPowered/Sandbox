package org.sandboxpowered.example;

import org.sandboxpowered.api.SandboxAPI;
import org.sandboxpowered.api.addon.Addon;
import org.sandboxpowered.api.block.*;
import org.sandboxpowered.api.events.BlockEvents;
import org.sandboxpowered.api.events.ItemEvents;
import org.sandboxpowered.api.item.BaseItem;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.item.Items;
import org.sandboxpowered.api.item.tool.ToolItem;
import org.sandboxpowered.api.item.tool.ToolMaterial;
import org.sandboxpowered.api.item.tool.ToolMaterials;
import org.sandboxpowered.api.item.tool.ToolType;
import org.sandboxpowered.api.registry.Registrar;
import org.sandboxpowered.api.resources.ResourceConstants;
import org.sandboxpowered.api.resources.ResourceMaterial;
import org.sandboxpowered.api.resources.ResourceService;
import org.sandboxpowered.api.util.InteractionResult;

import java.util.List;

public class Example implements Addon {
    @Override
    public void init(SandboxAPI api) {
        api.getLog().info("Loading Example Addon");

        BlockEvents.BREAK.subscribe((world, position, state, player, tool, cancellable) -> {
            if (state.is(Blocks.BEDROCK))
                cancellable.cancel();
        });

        ItemEvents.DRAW_STACK_COUNT.subscribe((stack, previous) -> {
            return previous && !Items.DIAMOND.matches(stack.getItem());
        });
        ItemEvents.SHOW_DURABILITY_BAR.subscribe((stack, previous) -> {
            return previous || Items.DIAMOND.matches(stack.getItem()) || stack.isDamageable();
        });
        ItemEvents.GET_DURABILITY_VALUE.subscribe((stack, previous) -> {
            if (Items.DIAMOND.matches(stack.getItem()))
                return stack.getCount() / (float) stack.getMaxCount();
            return previous;
        });
        ItemEvents.GET_DURABILITY_COLOR.subscribe((stack, previous) -> {
            if (Items.DIAMOND.matches(stack.getItem())) return 0x15CCEC;
            return previous;
        });

        BlockEvents.INTERACT.subscribe((world, position, state, player, hand, stack, result) -> {
            if (result != InteractionResult.IGNORE)
                return result;
            if (world.isClient())
                return InteractionResult.IGNORE;
            Block block = state.getBlock();
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
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.IGNORE;
        });
    }

    public void registerMaterials(ResourceService service) {
        service.add(ResourceConstants.IRON, ResourceConstants.INGOT);
        service.add(ResourceConstants.GOLD, ResourceConstants.INGOT);
        service.add(ResourceConstants.COPPER,
                ResourceConstants.INGOT,
                ResourceConstants.ORE,
                ResourceConstants.NUGGET,
                ResourceConstants.BLOCK
        );
    }

    @Override
    public void register(SandboxAPI api, Registrar registrar) {
        registrar.useRegistrarService(ResourceService.class, this::registerMaterials);
        registrar.register("test", new BaseItem(new Item.Settings()));
        registrar.register("block", new BaseBlock(Block.Settings.builder(Materials.STONE).setLuminance(-1).build()));
        registrar.register("block_no_itemblock", new BaseBlock(Block.Settings.builder(Materials.STONE).removeItemBlock().build()));

        registrar.register("wooden_paxel", createPaxel(ToolMaterials.WOOD, 6.0F, -3.2F));
        registrar.register("stone_paxel", createPaxel(ToolMaterials.STONE, 7.0F, -3.2F));
        registrar.register("iron_paxel", createPaxel(ToolMaterials.IRON, 6.0F, -3.1F));
        registrar.register("golden_paxel", createPaxel(ToolMaterials.GOLD, 6.0F, -3.0F));
        registrar.register("diamond_paxel", createPaxel(ToolMaterials.DIAMOND, 5.0F, -3.0F));
        registrar.register("netherite_paxel", createPaxel(ToolMaterials.NETHERITE, 5.0F, -3.0F));
    }

    public ToolItem createPaxel(ToolMaterial material, float damageIn, float speedIn) {
        return new ToolItem(material, damageIn, speedIn, new Item.Settings()
                .addToolTypes(material,
                        ToolType.PICKAXE,
                        ToolType.AXE,
                        ToolType.SHOVEL,
                        ToolType.STRIPPER,
                        ToolType.PAVER
                )
        );
    }
}