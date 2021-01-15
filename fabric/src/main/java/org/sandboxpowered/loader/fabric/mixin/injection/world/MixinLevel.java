package org.sandboxpowered.loader.fabric.mixin.injection.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.Side;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.BlockFlag;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.api.world.WorldReader;
import org.sandboxpowered.loader.Wrappers;
import org.spongepowered.asm.mixin.*;

@Mixin(Level.class)
@Implements({@Interface(iface = WorldReader.class, prefix = "reader$", remap = Interface.Remap.NONE), @Interface(iface = World.class, prefix = "world$", remap = Interface.Remap.NONE)})
@Unique
public abstract class MixinLevel implements LevelAccessor {
    @Shadow
    @Nullable
    public abstract MinecraftServer getServer();

    @Shadow public abstract boolean setBlock(BlockPos blockPos, net.minecraft.world.level.block.state.BlockState blockState, int i);

    public Side world$getSide() {
        return ((Object) this) instanceof ServerLevel ? Side.SERVER : Side.CLIENT;
    }

    public void world$spawnItem(double x, double y, double z, ItemStack stack) {
        ItemEntity entity = new ItemEntity(Level.class.cast(this), x, y, z, Wrappers.ITEMSTACK.toVanilla(stack));
        addFreshEntity(entity);
    }

    public boolean world$setBlockState(Position position, BlockState state, BlockFlag... flags) {
        return setBlock(
                Wrappers.POSITION.toVanilla(position),
                Wrappers.BLOCKSTATE.toVanilla(state),
                3
        );
    }
}