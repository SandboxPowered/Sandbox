package com.hrznstudio.sandbox.mixin.fabric.block;

import com.hrznstudio.sandbox.util.wrapper.BlockWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Block.class)
public class MixinBlock {
    /**
     * @author Coded
     * @reason Custom stone
     */
    @Overwrite
    public static boolean isNaturalStone(Block block_1) {
        if (block_1 == Blocks.STONE || block_1 == Blocks.GRANITE || block_1 == Blocks.DIORITE || block_1 == Blocks.ANDESITE)
            return true;
        if (block_1 instanceof BlockWrapper)
            return ((BlockWrapper) block_1).getBlock().isNaturalStone();
        return false;
    }

    /**
     * @author Coded
     * @reason Custom dirt
     */
    @Overwrite
    public static boolean isNaturalDirt(Block block_1) {
        if (block_1 == Blocks.DIRT || block_1 == Blocks.COARSE_DIRT || block_1 == Blocks.PODZOL)
            return true;
        if (block_1 instanceof BlockWrapper)
            return ((BlockWrapper) block_1).getBlock().isNaturalDirt();
        return false;
    }
}