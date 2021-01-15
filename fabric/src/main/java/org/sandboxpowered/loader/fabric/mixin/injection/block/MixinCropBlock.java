package org.sandboxpowered.loader.fabric.mixin.injection.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.loader.Wrappers;
import org.spongepowered.asm.mixin.*;

@Mixin(CropBlock.class)
@Implements(@Interface(iface = org.sandboxpowered.api.block.CropBlock.class, prefix = "crop$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinCropBlock {
    @Shadow
    public abstract net.minecraft.world.level.block.state.BlockState getStateForAge(int i);

    @Shadow
    public abstract int getMaxAge();

    @Shadow
    protected abstract int getAge(net.minecraft.world.level.block.state.BlockState blockState);

    @Shadow
    protected abstract ItemLike getBaseSeedId();

    public Item crop$getSeed() {
        return Wrappers.ITEM.toSandbox(getBaseSeedId().asItem());
    }

    public int crop$getAge(BlockState state) {
        return getAge(Wrappers.BLOCKSTATE.toVanilla(state));
    }

    public int crop$getMaxAge() {
        return getMaxAge();
    }

    public BlockState crop$stateForAge(int age) {
        return Wrappers.BLOCKSTATE.toSandbox(getStateForAge(age));
    }
}