package org.sandboxpowered.loader.fabric.mixin.injection.block;

import net.minecraft.server.level.ServerLevel;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.loader.Wrappers;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collections;
import java.util.List;

@Mixin(net.minecraft.world.level.block.Block.class)
@Implements(@Interface(iface = Block.class, prefix = "block$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinBlock {

    public List<ItemStack> block$getDrops(World world, Position position, BlockState state) {
        if (!(world instanceof ServerLevel))
            return Collections.emptyList();
        return (List<ItemStack>) (Object) net.minecraft.world.level.block.Block.getDrops(
                Wrappers.BLOCKSTATE.toVanilla(state),
                (ServerLevel) world,
                Wrappers.POSITION.toVanilla(position),
                null
        );
    }
}
