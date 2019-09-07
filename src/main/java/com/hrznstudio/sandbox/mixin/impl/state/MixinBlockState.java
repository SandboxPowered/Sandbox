package com.hrznstudio.sandbox.mixin.impl.state;

import com.google.common.collect.ImmutableMap;
import com.hrznstudio.sandbox.api.block.IBlock;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.block.BlockState;
import net.minecraft.state.AbstractPropertyContainer;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.*;

@Mixin(BlockState.class)
@Implements(@Interface(iface = com.hrznstudio.sandbox.api.state.BlockState.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinBlockState extends AbstractPropertyContainer<net.minecraft.block.Block, BlockState> {
    public MixinBlockState(net.minecraft.block.Block object_1, ImmutableMap<Property<?>, Comparable<?>> immutableMap_1) {
        super(object_1, immutableMap_1);
    }

    @Shadow
    public abstract net.minecraft.block.Block getBlock();

    public IBlock sbx$getBlock() {
        return WrappingUtil.convert(this.owner); // Has to use the field otherwise causes a loop
    }
}