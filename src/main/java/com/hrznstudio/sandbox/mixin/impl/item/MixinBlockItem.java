package com.hrznstudio.sandbox.mixin.impl.item;

import com.hrznstudio.sandbox.api.block.IBlock;
import com.hrznstudio.sandbox.api.item.IBlockItem;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.item.BlockItem;
import org.spongepowered.asm.mixin.*;

@Mixin(BlockItem.class)
@Implements(@Interface(iface = IBlockItem.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinBlockItem {
    @Shadow
    public abstract net.minecraft.block.Block getBlock();

    public IBlock sbx$asBlock() {
        return WrappingUtil.convert(getBlock());
    }
}