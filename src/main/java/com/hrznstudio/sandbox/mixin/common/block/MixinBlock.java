package com.hrznstudio.sandbox.mixin.common.block;

import com.hrznstudio.sandbox.api.block.Block;
import com.hrznstudio.sandbox.api.block.state.BlockState;
import com.hrznstudio.sandbox.api.entity.Entity;
import com.hrznstudio.sandbox.api.entity.player.Hand;
import com.hrznstudio.sandbox.api.util.Activation;
import com.hrznstudio.sandbox.api.util.Direction;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.util.math.Vec3f;
import com.hrznstudio.sandbox.api.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(net.minecraft.block.Block.class)
public class MixinBlock implements Block {
    @Override
    public Activation onBlockUsed(World world, Position pos, BlockState state, Entity player, Hand hand, Direction side, Vec3f hit) {
        return Activation.IGNORE;
    }

    @Override
    public Activation onBlockClicked(World world, Position pos, BlockState state, Entity player, Direction side) {
        return Activation.IGNORE;
    }

    @Override
    public void onBlockPlaced(World world, Position position, BlockState state) {

    }

    @Override
    public void onBlockDestroyed(World world, Position position, BlockState state) {

    }
}