package org.sandboxpowered.sandbox.fabric.mixin.impl.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableWorld;
import org.sandboxpowered.sandbox.api.state.BlockState;
import org.sandboxpowered.sandbox.api.util.math.Position;
import org.sandboxpowered.sandbox.api.world.BlockFlag;
import org.sandboxpowered.sandbox.api.world.WorldWriter;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModifiableWorld.class)
public interface MixinModifiableWorld extends WorldWriter {
    @Shadow
    boolean setBlockState(BlockPos var1, net.minecraft.block.BlockState var2, int var3);

    @Override
    default boolean setBlockState(Position position, BlockState state, BlockFlag... flags) {
        return setBlockState(WrappingUtil.convert(position), WrappingUtil.convert(state), WrappingUtil.convert(flags));
    }
}