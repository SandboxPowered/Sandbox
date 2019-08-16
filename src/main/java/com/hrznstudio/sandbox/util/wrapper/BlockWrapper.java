package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.api.block.IBlock;
import com.hrznstudio.sandbox.api.entity.Entity;
import com.hrznstudio.sandbox.api.item.Stack;
import com.hrznstudio.sandbox.api.util.InteractionResult;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.WorldReader;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockWrapper extends Block {
    private IBlock block;

    public BlockWrapper(IBlock block) {
        super(WrappingUtil.convert(block.createProperties()));
        this.block = block;
    }

    public static BlockWrapper create(IBlock block) {
        if (block.hasBlockEntity()) {
            return new BlockWrapper.WithBlockEntity(block);
        }
        return new BlockWrapper(block);
    }

    public IBlock getBlock() {
        return block;
    }

    @Override
    public boolean activate(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1, Hand hand_1, BlockHitResult blockHitResult_1) {
        return block.onBlockUsed(
                (com.hrznstudio.sandbox.api.world.World) world_1,
                (Position) blockPos_1,
                (com.hrznstudio.sandbox.api.block.state.BlockState) blockState_1,
                null,
                hand_1 == Hand.MAIN_HAND ? com.hrznstudio.sandbox.api.entity.player.Hand.MAIN_HAND : com.hrznstudio.sandbox.api.entity.player.Hand.OFF_HAND,
                null,
                null
        ) != InteractionResult.IGNORE;
    }

    @Override
    public void onPlaced(World world_1, BlockPos blockPos_1, BlockState blockState_1, @Nullable LivingEntity livingEntity_1, ItemStack itemStack_1) {
        block.onBlockPlaced(
                (com.hrznstudio.sandbox.api.world.World) world_1,
                (Position) blockPos_1,
                (com.hrznstudio.sandbox.api.block.state.BlockState) blockPos_1,
                (Entity) livingEntity_1,
                WrappingUtil.cast(itemStack_1, Stack.class)
        );
    }

    @Override
    public void onBroken(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1) {
        block.onBlockDestroyed(
                (com.hrznstudio.sandbox.api.world.World) iWorld_1.getWorld(),
                (Position) blockPos_1,
                (com.hrznstudio.sandbox.api.block.state.BlockState) blockState_1
        );
    }

    public static class WithBlockEntity extends BlockWrapper implements BlockEntityProvider {
        public WithBlockEntity(IBlock block) {
            super(block);
        }

        @Nullable
        @Override
        public BlockEntity createBlockEntity(BlockView var1) {
            return WrappingUtil.convert(getBlock().createBlockEntity((WorldReader) var1));
        }
    }
}