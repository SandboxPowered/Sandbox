package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.api.block.IBlock;
import com.hrznstudio.sandbox.api.entity.IEntity;
import com.hrznstudio.sandbox.api.entity.player.Player;
import com.hrznstudio.sandbox.api.item.ItemStack;
import com.hrznstudio.sandbox.api.util.InteractionResult;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.util.math.Vec3f;
import com.hrznstudio.sandbox.api.world.WorldReader;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockWrapper extends Block {
    private IBlock block;

    public BlockWrapper(IBlock block) {
        super(WrappingUtil.convert(block.getProperties()));
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
                (Player) playerEntity_1,
                hand_1 == Hand.MAIN_HAND ? com.hrznstudio.sandbox.api.entity.player.Hand.MAIN_HAND : com.hrznstudio.sandbox.api.entity.player.Hand.OFF_HAND,
                WrappingUtil.convert(blockHitResult_1.getSide()),
                (Vec3f) (Object) new Vector3f(blockHitResult_1.getPos())
        ) != InteractionResult.IGNORE;
    }

    @Override
    public void onBlockBreakStart(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1) {
        block.onBlockClicked(
                (com.hrznstudio.sandbox.api.world.World) world_1,
                (Position) blockPos_1,
                (com.hrznstudio.sandbox.api.block.state.BlockState) blockState_1,
                (Player) playerEntity_1
        )
    }

    @Override
    public void onPlaced(World world_1, BlockPos blockPos_1, BlockState blockState_1, @Nullable LivingEntity livingEntity_1, net.minecraft.item.ItemStack itemStack_1) {
        block.onBlockPlaced(
                (com.hrznstudio.sandbox.api.world.World) world_1,
                (Position) blockPos_1,
                (com.hrznstudio.sandbox.api.block.state.BlockState) blockPos_1,
                (IEntity) livingEntity_1,
                WrappingUtil.cast(itemStack_1, ItemStack.class)
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

    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockState_1, Direction direction_1, BlockState blockState_2, IWorld iWorld_1, BlockPos blockPos_1, BlockPos blockPos_2) {
        return WrappingUtil.convert(block.updateOnNeighborChanged(
                (com.hrznstudio.sandbox.api.block.state.BlockState) blockState_1,
                WrappingUtil.convert(direction_1),
                (com.hrznstudio.sandbox.api.block.state.BlockState) blockState_2,
                (com.hrznstudio.sandbox.api.world.World) iWorld_1.getWorld(),
                (Position) blockPos_1,
                (Position) blockPos_2
        ));
    }

    @Override
    public BlockState rotate(BlockState blockState_1, BlockRotation blockRotation_1) {
        return WrappingUtil.convert(block.rotate(
                (com.hrznstudio.sandbox.api.block.state.BlockState) blockState_1,
                WrappingUtil.convert(blockRotation_1)
        ));
    }

    @Override
    public BlockState mirror(BlockState blockState_1, BlockMirror blockMirror_1) {
        return WrappingUtil.convert(block.mirror(
                (com.hrznstudio.sandbox.api.block.state.BlockState) blockState_1,
                WrappingUtil.convert(blockMirror_1)
        ));
    }

    @Override
    public boolean canReplace(BlockState blockState_1, ItemPlacementContext itemPlacementContext_1) {
        return block.canReplace((com.hrznstudio.sandbox.api.block.state.BlockState) blockState_1);
    }

    @Override
    public boolean isAir(BlockState blockState_1) {
        return block.isAir((com.hrznstudio.sandbox.api.block.state.BlockState) blockState_1);
    }

    @Override
    public void onSteppedOn(World world_1, BlockPos blockPos_1, Entity entity_1) {
        block.onEntityWalk(
                (com.hrznstudio.sandbox.api.world.World) world_1,
                (Position) blockPos_1,
                WrappingUtil.convert(entity_1)
        );
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState blockState_1) {
        return WrappingUtil.convert(block.getPistonInteraction((com.hrznstudio.sandbox.api.block.state.BlockState) blockState_1));
    }

    @Override
    public boolean canMobSpawnInside() {
        return block.canEntitySpawnWithin();
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