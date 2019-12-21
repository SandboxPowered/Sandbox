package org.sandboxpowered.sandbox.fabric.mixin.impl.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.sandboxpowered.sandbox.api.block.Block;
import org.sandboxpowered.sandbox.api.item.ItemStack;
import org.sandboxpowered.sandbox.api.state.BlockState;
import org.sandboxpowered.sandbox.api.util.math.Position;
import org.sandboxpowered.sandbox.api.world.WorldReader;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.block.Block.class)
@Implements(@Interface(iface = Block.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinBlockClient {
    @Shadow
    public abstract net.minecraft.item.ItemStack getPickStack(BlockView blockView_1, BlockPos blockPos_1, net.minecraft.block.BlockState blockState_1);

    public ItemStack sbx$getPickStack(WorldReader reader, Position position, BlockState state) {
        return WrappingUtil.cast(getPickStack(
                (BlockView) reader,
                (BlockPos) position,
                (net.minecraft.block.BlockState) state
        ), ItemStack.class);
    }
}