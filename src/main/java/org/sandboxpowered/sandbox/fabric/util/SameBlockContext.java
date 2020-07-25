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
    public SameBlockContext(ItemUsageContext itemUsageContext_1) {
        super(itemUsageContext_1);
        this.canReplaceExisting = true;
    }

    public SameBlockContext(World world_1, @Nullable PlayerEntity playerEntity_1, Hand hand_1, ItemStack itemStack_1, BlockHitResult blockHitResult_1) {
        super(world_1, playerEntity_1, hand_1, itemStack_1, blockHitResult_1);
        this.canReplaceExisting = true;
    }
}
