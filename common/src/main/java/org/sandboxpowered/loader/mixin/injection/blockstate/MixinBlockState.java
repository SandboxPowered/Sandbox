package org.sandboxpowered.loader.mixin.injection.blockstate;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.loader.Wrappers;
import org.spongepowered.asm.mixin.*;

@Mixin(BlockBehaviour.BlockStateBase.class)
@Implements(@Interface(iface = BlockState.class, prefix = "state$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinBlockState {
    @Shadow public abstract net.minecraft.world.level.block.Block getBlock();

    public Block state$getBlock() {
        return Wrappers.BLOCK.toSandbox(getBlock());
    }
}
