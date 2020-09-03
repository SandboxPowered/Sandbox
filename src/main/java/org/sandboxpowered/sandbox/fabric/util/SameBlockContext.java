package org.sandboxpowered.sandbox.fabric.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SameBlockContext extends ItemPlacementContext {
    public SameBlockContext(ItemUsageContext context) {
        super(context);
        this.canReplaceExisting = true;
    }

    public SameBlockContext(PlayerEntity player, Hand hand, ItemStack stack, BlockHitResult result) {
        super(player, hand, stack, result);
        this.canReplaceExisting = true;
    }

    public SameBlockContext(World world, @Nullable PlayerEntity player, Hand hand, ItemStack stack, BlockHitResult result) {
        super(world, player, hand, stack, result);
        this.canReplaceExisting = true;
    }
}
