package org.sandboxpowered.sandbox.fabric.util.wrapper;

import org.sandboxpowered.sandbox.api.SandboxInternal;
import org.sandboxpowered.sandbox.api.block.Block;
import org.sandboxpowered.sandbox.api.block.BaseBlock;
import org.sandboxpowered.sandbox.api.entity.Entity;
import org.sandboxpowered.sandbox.api.entity.player.Player;
import org.sandboxpowered.sandbox.api.item.ItemStack;
import org.sandboxpowered.sandbox.api.util.InteractionResult;
import org.sandboxpowered.sandbox.api.util.math.Position;
import org.sandboxpowered.sandbox.api.util.math.Vec3f;
import org.sandboxpowered.sandbox.api.world.WorldReader;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
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

public class BlockWrapper extends net.minecraft.block.Block implements SandboxInternal.BlockWrapper {
    private Block block;

    public BlockWrapper(Block block) {
        super(WrappingUtil.convert(block.getSettings()));
        this.block = block;
        if (this.block instanceof BaseBlock)
            ((BaseBlock) this.block).setStateFactory(((SandboxInternal.StateFactoryHolder) this).getSandboxStateFactory());
    }

    public static SandboxInternal.BlockWrapper create(Block block) {
        if (block instanceof org.sandboxpowered.sandbox.api.block.FluidBlock)
            return new WithFluid((BaseFluid) WrappingUtil.convert(((org.sandboxpowered.sandbox.api.block.FluidBlock) block).getFluid()), block);
        if (block.canContainFluids()) {
            if (block.hasBlockEntity()) {
                return new BlockWrapper.WithWaterloggableBlockEntity(block);
            }
            return new BlockWrapper.WithWaterloggable(block);
        }
        if (block.hasBlockEntity()) {
            return new BlockWrapper.WithBlockEntity(block);
        }
        return new BlockWrapper(block);
    }

    @Override
    protected void appendProperties(StateFactory.Builder<net.minecraft.block.Block, BlockState> stateFactory$Builder_1) {
        super.appendProperties(stateFactory$Builder_1);
        if (block instanceof BaseBlock)
            ((BaseBlock) block).appendProperties(((SandboxInternal.StateFactoryBuilder) stateFactory$Builder_1).getSboxBuilder());
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public boolean activate(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1, Hand hand_1, BlockHitResult blockHitResult_1) {
        return block.onBlockUsed(
                (org.sandboxpowered.sandbox.api.world.World) world_1,
                (Position) blockPos_1,
                (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1,
                (Player) playerEntity_1,
                hand_1 == Hand.MAIN_HAND ? org.sandboxpowered.sandbox.api.entity.player.Hand.MAIN_HAND : org.sandboxpowered.sandbox.api.entity.player.Hand.OFF_HAND,
                WrappingUtil.convert(blockHitResult_1.getSide()),
                (Vec3f) (Object) new Vector3f(blockHitResult_1.getPos())
        ) != InteractionResult.IGNORE;
    }

    @Override
    public void onBlockBreakStart(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1) {
        block.onBlockClicked(
                (org.sandboxpowered.sandbox.api.world.World) world_1,
                (Position) blockPos_1,
                (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1,
                (Player) playerEntity_1
        );
    }

    @Override
    public void onPlaced(World world_1, BlockPos blockPos_1, BlockState blockState_1, @Nullable LivingEntity livingEntity_1, net.minecraft.item.ItemStack itemStack_1) {
        block.onBlockPlaced(
                (org.sandboxpowered.sandbox.api.world.World) world_1,
                (Position) blockPos_1,
                (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1,
                (Entity) livingEntity_1,
                WrappingUtil.cast(itemStack_1, ItemStack.class)
        );
    }

    @Override
    public void onBroken(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1) {
        block.onBlockBroken(
                (org.sandboxpowered.sandbox.api.world.World) iWorld_1.getWorld(),
                (Position) blockPos_1,
                (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1
        );
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockState_1, Direction direction_1, BlockState blockState_2, IWorld iWorld_1, BlockPos blockPos_1, BlockPos blockPos_2) {
        return WrappingUtil.convert(block.updateOnNeighborChanged(
                (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1,
                WrappingUtil.convert(direction_1),
                (org.sandboxpowered.sandbox.api.state.BlockState) blockState_2,
                (org.sandboxpowered.sandbox.api.world.World) iWorld_1.getWorld(),
                (Position) blockPos_1,
                (Position) blockPos_2
        ));
    }

    @Override
    public BlockState rotate(BlockState blockState_1, BlockRotation blockRotation_1) {
        return WrappingUtil.convert(block.rotate(
                (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1,
                WrappingUtil.convert(blockRotation_1)
        ));
    }

    @Override
    public BlockState mirror(BlockState blockState_1, BlockMirror blockMirror_1) {
        return WrappingUtil.convert(block.mirror(
                (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1,
                WrappingUtil.convert(blockMirror_1)
        ));
    }

    @Override
    public boolean canReplace(BlockState blockState_1, ItemPlacementContext itemPlacementContext_1) {
        return block.canReplace((org.sandboxpowered.sandbox.api.state.BlockState) blockState_1);
    }

    @Override
    public boolean isAir(BlockState blockState_1) {
        return block.isAir((org.sandboxpowered.sandbox.api.state.BlockState) blockState_1);
    }

    @Override
    public void onSteppedOn(World world_1, BlockPos blockPos_1, net.minecraft.entity.Entity entity_1) {
        block.onEntityWalk(
                (org.sandboxpowered.sandbox.api.world.World) world_1,
                (Position) blockPos_1,
                WrappingUtil.convert(entity_1)
        );
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState blockState_1) {
        return WrappingUtil.convert(block.getPistonInteraction((org.sandboxpowered.sandbox.api.state.BlockState) blockState_1));
    }

    @Override
    public boolean canMobSpawnInside() {
        return block.canEntitySpawnWithin();
    }

    public static class WithFluid extends FluidBlock implements SandboxInternal.BlockWrapper {
        private Block block;

        public WithFluid(BaseFluid baseFluid_1, Block block) {
            super(baseFluid_1, WrappingUtil.convert(block.getSettings()));
            this.block = block;
            if (this.block instanceof BaseBlock)
                ((BaseBlock) this.block).setStateFactory(((SandboxInternal.StateFactoryHolder) this).getSandboxStateFactory());
        }

        @Override
        protected void appendProperties(StateFactory.Builder<net.minecraft.block.Block, BlockState> stateFactory$Builder_1) {
            super.appendProperties(stateFactory$Builder_1);
            if (block instanceof org.sandboxpowered.sandbox.api.block.FluidBlock)
                ((BaseBlock) block).appendProperties(((SandboxInternal.StateFactoryBuilder) stateFactory$Builder_1).getSboxBuilder());
        }

        @Override
        public Block getBlock() {
            return block;
        }

        @Override
        public boolean activate(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1, Hand hand_1, BlockHitResult blockHitResult_1) {
            return block.onBlockUsed(
                    (org.sandboxpowered.sandbox.api.world.World) world_1,
                    (Position) blockPos_1,
                    (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1,
                    (Player) playerEntity_1,
                    hand_1 == Hand.MAIN_HAND ? org.sandboxpowered.sandbox.api.entity.player.Hand.MAIN_HAND : org.sandboxpowered.sandbox.api.entity.player.Hand.OFF_HAND,
                    WrappingUtil.convert(blockHitResult_1.getSide()),
                    (Vec3f) (Object) new Vector3f(blockHitResult_1.getPos())
            ) != InteractionResult.IGNORE;
        }

        @Override
        public void onBlockBreakStart(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1) {
            block.onBlockClicked(
                    (org.sandboxpowered.sandbox.api.world.World) world_1,
                    (Position) blockPos_1,
                    (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1,
                    (Player) playerEntity_1
            );
        }

        @Override
        public void onPlaced(World world_1, BlockPos blockPos_1, BlockState blockState_1, @Nullable LivingEntity livingEntity_1, net.minecraft.item.ItemStack itemStack_1) {
            block.onBlockPlaced(
                    (org.sandboxpowered.sandbox.api.world.World) world_1,
                    (Position) blockPos_1,
                    (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1,
                    (Entity) livingEntity_1,
                    WrappingUtil.cast(itemStack_1, ItemStack.class)
            );
        }

        @Override
        public void onBroken(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1) {
            block.onBlockBroken(
                    (org.sandboxpowered.sandbox.api.world.World) iWorld_1.getWorld(),
                    (Position) blockPos_1,
                    (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1
            );
        }

        @Override
        public BlockState getStateForNeighborUpdate(BlockState blockState_1, Direction direction_1, BlockState blockState_2, IWorld iWorld_1, BlockPos blockPos_1, BlockPos blockPos_2) {
            return WrappingUtil.convert(block.updateOnNeighborChanged(
                    (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1,
                    WrappingUtil.convert(direction_1),
                    (org.sandboxpowered.sandbox.api.state.BlockState) blockState_2,
                    (org.sandboxpowered.sandbox.api.world.World) iWorld_1.getWorld(),
                    (Position) blockPos_1,
                    (Position) blockPos_2
            ));
        }

        @Override
        public BlockState rotate(BlockState blockState_1, BlockRotation blockRotation_1) {
            return WrappingUtil.convert(block.rotate(
                    (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1,
                    WrappingUtil.convert(blockRotation_1)
            ));
        }

        @Override
        public BlockState mirror(BlockState blockState_1, BlockMirror blockMirror_1) {
            return WrappingUtil.convert(block.mirror(
                    (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1,
                    WrappingUtil.convert(blockMirror_1)
            ));
        }

        @Override
        public boolean canReplace(BlockState blockState_1, ItemPlacementContext itemPlacementContext_1) {
            return block.canReplace((org.sandboxpowered.sandbox.api.state.BlockState) blockState_1);
        }

        @Override
        public boolean isAir(BlockState blockState_1) {
            return block.isAir((org.sandboxpowered.sandbox.api.state.BlockState) blockState_1);
        }

        @Override
        public void onSteppedOn(World world_1, BlockPos blockPos_1, net.minecraft.entity.Entity entity_1) {
            block.onEntityWalk(
                    (org.sandboxpowered.sandbox.api.world.World) world_1,
                    (Position) blockPos_1,
                    WrappingUtil.convert(entity_1)
            );
        }

        @Override
        public PistonBehavior getPistonBehavior(BlockState blockState_1) {
            return WrappingUtil.convert(block.getPistonInteraction((org.sandboxpowered.sandbox.api.state.BlockState) blockState_1));
        }

        @Override
        public boolean canMobSpawnInside() {
            return block.canEntitySpawnWithin();
        }
    }

    public static class WithBlockEntity extends BlockWrapper implements BlockEntityProvider {
        public WithBlockEntity(Block block) {
            super(block);
        }

        @Nullable
        @Override
        public BlockEntity createBlockEntity(BlockView var1) {
            return WrappingUtil.convert(getBlock().createBlockEntity((WorldReader) var1));
        }
    }

    public static class WithWaterloggable extends BlockWrapper implements Waterloggable {
        public WithWaterloggable(Block block) {
            super(block);
        }

        @Override
        public boolean canFillWithFluid(BlockView blockView_1, BlockPos blockPos_1, BlockState blockState_1, Fluid fluid_1) {
            return getBlock().canContainFluid(
                    (WorldReader) blockView_1,
                    (Position) blockPos_1,
                    (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1,
                    WrappingUtil.convert(fluid_1)
            );
        }

        @Override
        public boolean tryFillWithFluid(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1, FluidState fluidState_1) {
            return getBlock().fillWith(
                    (org.sandboxpowered.sandbox.api.world.World) iWorld_1,
                    (Position) blockPos_1,
                    (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1,
                    (org.sandboxpowered.sandbox.api.state.FluidState) fluidState_1
            );
        }

        @Override
        public Fluid tryDrainFluid(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1) {
            return WrappingUtil.convert(getBlock().drainFrom(
                    (org.sandboxpowered.sandbox.api.world.World) iWorld_1,
                    (Position) blockPos_1,
                    (org.sandboxpowered.sandbox.api.state.BlockState) blockState_1
            ));
        }
    }

    public static class WithWaterloggableBlockEntity extends BlockWrapper.WithWaterloggable implements BlockEntityProvider {
        public WithWaterloggableBlockEntity(Block block) {
            super(block);
        }

        @Nullable
        @Override
        public BlockEntity createBlockEntity(BlockView var1) {
            return WrappingUtil.convert(getBlock().createBlockEntity((WorldReader) var1));
        }
    }
}