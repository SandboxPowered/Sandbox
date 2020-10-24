package org.sandboxpowered.sandbox.fabric.mixin.impl.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.WorldReader;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(net.minecraft.block.Block.class)
@Implements(@Interface(iface = Block.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
@SuppressWarnings({"java:S100", "java:S1610"})
public abstract class MixinBlockClient {
    @Shadow
    public abstract net.minecraft.item.ItemStack getPickStack(BlockView world, BlockPos pos, net.minecraft.block.BlockState state);

    public ItemStack sbx$getPickStack(WorldReader reader, Position position, BlockState state) {
        return WrappingUtil.cast(getPickStack(
                (BlockView) reader,
                (BlockPos) position,
                (net.minecraft.block.BlockState) state
        ), ItemStack.class);
    }
}