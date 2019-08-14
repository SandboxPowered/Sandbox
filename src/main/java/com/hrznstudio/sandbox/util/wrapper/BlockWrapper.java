package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.api.util.Activation;
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
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockWrapper extends Block {
    private com.hrznstudio.sandbox.api.block.Block block;

    public BlockWrapper(com.hrznstudio.sandbox.api.block.Block block) {
        super(WrappingUtil.convert(block.createProperties()));
        this.block = block;
    }

    public static BlockWrapper create(com.hrznstudio.sandbox.api.block.Block block) {
        if (block.hasBlockEntity()) {
            return new BlockWrapper.WithBlockEntity(block);
        }
        return new BlockWrapper(block);
    }

    public com.hrznstudio.sandbox.api.block.Block getBlock() {
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
        ) != Activation.IGNORE;
    }

    @Override
    public void onPlaced(World world_1, BlockPos blockPos_1, BlockState blockState_1, @Nullable LivingEntity livingEntity_1, ItemStack itemStack_1) {
        block.onBlockPlaced(
                (com.hrznstudio.sandbox.api.world.World) world_1,
                (Position) blockPos_1,
                (com.hrznstudio.sandbox.api.block.state.BlockState) blockPos_1
        );
    }

    public static class WithBlockEntity extends BlockWrapper implements BlockEntityProvider {
        public WithBlockEntity(com.hrznstudio.sandbox.api.block.Block block) {
            super(block);
        }

        @Nullable
        @Override
        public BlockEntity createBlockEntity(BlockView var1) {
            return WrappingUtil.convert(getBlock().createBlockEntity((WorldReader) var1));
        }
    }
}