package org.sandboxpowered.loader.mixin.injection.blockstate;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.loader.Wrappers;
import org.spongepowered.asm.mixin.*;

@Mixin(BlockBehaviour.BlockStateBase.class)
@Implements(@Interface(iface = BlockState.class, prefix = "state$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinBlockState {
    @Shadow
    public abstract net.minecraft.world.level.block.Block getBlock();

    @Shadow
    public abstract float getDestroySpeed(BlockGetter blockGetter, BlockPos blockPos);

    @NotNull
    public Block state$getBlock() {
        return Wrappers.BLOCK.toSandbox(getBlock());
    }

    public float state$getDestroySpeed(@NotNull World world,@NotNull Position position) {
        return getDestroySpeed(
                Wrappers.WORLD.toVanilla(world),
                Wrappers.POSITION.toVanilla(position)
        );
    }
}
