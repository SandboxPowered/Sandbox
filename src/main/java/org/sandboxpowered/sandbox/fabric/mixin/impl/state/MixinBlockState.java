package org.sandboxpowered.sandbox.fabric.mixin.impl.state;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.state.AbstractState;
import net.minecraft.state.property.Property;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(BlockState.class)
@Implements(@Interface(iface = org.sandboxpowered.api.state.BlockState.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinBlockState extends AbstractState<net.minecraft.block.Block, BlockState> {
    public MixinBlockState(net.minecraft.block.Block object_1, ImmutableMap<Property<?>, Comparable<?>> immutableMap_1) {
        super(object_1, immutableMap_1);
    }

    @Shadow
    public abstract net.minecraft.block.Block getBlock();

    public Block sbx$getBlock() {
        return WrappingUtil.convert(this.owner); // Has to use the field otherwise causes a loop
    }
}