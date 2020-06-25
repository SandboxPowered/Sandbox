package org.sandboxpowered.sandbox.fabric.mixin.impl.state;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockState.class)
@Implements(@Interface(iface = org.sandboxpowered.api.state.BlockState.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinBlockState extends State<net.minecraft.block.Block, BlockState> {
    public MixinBlockState(net.minecraft.block.Block object, ImmutableMap<Property<?>, Comparable<?>> immutableMap, MapCodec<BlockState> mapCodec) {
        super(object, immutableMap, mapCodec);
    }

    public Block sbx$getBlock() {
        return WrappingUtil.convert(this.owner); // Has to use the field otherwise causes a loop
    }
}