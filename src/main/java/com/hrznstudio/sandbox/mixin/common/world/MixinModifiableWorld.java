package com.hrznstudio.sandbox.mixin.common.world;

import com.hrznstudio.sandbox.api.block.state.BlockState;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.BlockFlag;
import com.hrznstudio.sandbox.api.world.WorldWriter;
import com.hrznstudio.sandbox.util.ConversionUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModifiableWorld.class)
public interface MixinModifiableWorld extends WorldWriter {
    @Shadow
    boolean setBlockState(BlockPos var1, net.minecraft.block.BlockState var2, int var3);

    @Override
    default boolean setBlockState(Position position, BlockState state, BlockFlag... flags) {
        return setBlockState(ConversionUtil.convert(position), ConversionUtil.convert(state), ConversionUtil.convert(flags));
    }
}