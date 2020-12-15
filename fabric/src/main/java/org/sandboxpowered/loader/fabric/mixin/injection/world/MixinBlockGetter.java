package org.sandboxpowered.loader.fabric.mixin.injection.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.sandboxpowered.api.entity.Entity;
import org.sandboxpowered.api.shape.Box;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.state.FluidState;
import org.sandboxpowered.api.tags.TagManager;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.WorldReader;
import org.sandboxpowered.loader.Wrappers;
import org.spongepowered.asm.mixin.*;

import java.util.stream.Stream;

@Mixin(BlockGetter.class)
@Implements(@Interface(iface = WorldReader.class, prefix = "reader$", remap = Interface.Remap.NONE))
@Unique
public interface MixinBlockGetter {
    @Shadow net.minecraft.world.level.block.state.BlockState getBlockState(BlockPos blockPos);

    @Shadow @Nullable net.minecraft.world.level.block.entity.BlockEntity getBlockEntity(BlockPos blockPos);

    default BlockState reader$getBlockState(Position position) {
        return Wrappers.BLOCKSTATE.toSandbox(getBlockState(Wrappers.POSITION.toVanilla(position)));
    }

    default @Nullable BlockEntity reader$getBlockEntity(Position position) {
        return Wrappers.BLOCK_ENTITY.toSandbox(getBlockEntity(Wrappers.POSITION.toVanilla(position)));
    }

    default FluidState reader$getFluidState(Position position) {
        return null;
    }

    default Stream<Entity> reader$getEntitiesWithin(Box box) {
        return Stream.empty();
    }

    default <T extends Entity> Stream<T> reader$getEntitiesWithin(Box box, Class<T> filter) {
        return Stream.empty();
    }

    default long reader$getWorldTime() {
        return 0;
    }

    default TagManager reader$getTagManager() {
        return null;
    }
}
