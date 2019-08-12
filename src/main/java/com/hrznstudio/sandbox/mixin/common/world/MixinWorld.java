package com.hrznstudio.sandbox.mixin.common.world;

import com.hrznstudio.sandbox.api.block.state.BlockState;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.util.ConversionUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;

@Mixin(World.class)
@Implements(@Interface(iface = com.hrznstudio.sandbox.api.world.World.class, prefix = "sbx$"))
public abstract class MixinWorld {
    @Shadow
    public abstract net.minecraft.block.BlockState getBlockState(BlockPos blockPos_1);

    @Shadow public abstract boolean isClient();

    public BlockState sbx$getBlockState(Position position) {
        return (BlockState) this.getBlockState(ConversionUtil.convertPosition(position));
    }

    @Unique
    public boolean sbx$isClient() {
        return isClient();
    }
}