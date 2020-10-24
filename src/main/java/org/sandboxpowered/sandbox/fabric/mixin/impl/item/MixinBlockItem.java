package org.sandboxpowered.sandbox.fabric.mixin.impl.item;

import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.item.BlockItem;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.item.BlockItem.class)
@Implements(@Interface(iface = BlockItem.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
@SuppressWarnings({"java:S100","java:S1610"})
public abstract class MixinBlockItem {
    @Shadow
    public abstract net.minecraft.block.Block getBlock();

    public Block sbx$asBlock() {
        return WrappingUtil.convert(getBlock());
    }
}