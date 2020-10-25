package org.sandboxpowered.sandbox.fabric.mixin.impl.block;

import net.minecraft.block.AbstractBlock;
import org.sandboxpowered.api.block.Block;
import org.spongepowered.asm.mixin.*;

@Mixin(AbstractBlock.class)
@Implements(@Interface(iface = Block.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@SuppressWarnings({"java:S100", "java:S1610"})
public abstract class MixinAbstractBlock {
    @Shadow
    public abstract boolean hasBlockEntity();

    @Intrinsic
    public boolean sbx$hasBlockEntity() {
        return this.hasBlockEntity();
    }
}