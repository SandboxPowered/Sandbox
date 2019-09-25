package org.sandboxpowered.sandbox.fabric.mixin.impl.item;

import org.sandboxpowered.sandbox.api.block.Block;
import org.sandboxpowered.sandbox.api.item.BlockItem;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.item.BlockItem.class)
@Implements(@Interface(iface = BlockItem.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinBlockItem {
    @Shadow
    public abstract net.minecraft.block.Block getBlock();

    public Block sbx$asBlock() {
        return WrappingUtil.convert(getBlock());
    }
}