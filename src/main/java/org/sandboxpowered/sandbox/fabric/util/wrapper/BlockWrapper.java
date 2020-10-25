package org.sandboxpowered.sandbox.fabric.util.wrapper;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.block.BaseBlock;
import org.sandboxpowered.api.block.Block;
import org.sandboxpowered.api.component.Components;
import org.sandboxpowered.api.component.FluidContainer;
import org.sandboxpowered.api.entity.Entity;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.sandboxpowered.api.fluid.FluidStack;
import org.sandboxpowered.api.fluid.Fluids;
import org.sandboxpowered.api.state.StateFactory;
import org.sandboxpowered.api.util.InteractionResult;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.util.math.Vec3f;
import org.sandboxpowered.api.world.WorldReader;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

@SuppressWarnings("unchecked")
public class BlockWrapper extends net.minecraft.block.Block implements SandboxInternal.IBlockWrapper {
    public final Block block;
    public final StateManager<net.minecraft.block.Block, BlockState> wrappedStateManager;
    private final StateFactory<Block, org.sandboxpowered.api.state.BlockState> sandboxWrappedFactory;

    public BlockWrapper(Block block) {
        super(WrappingUtil.convert(block.getSettings()));
        this.block = block;
        StateManager.Builder<net.minecraft.block.Block, BlockState> builder = new StateManager.Builder<>(this);
        this.appendProperties(builder);
        this.wrappedStateManager = builder.build(net.minecraft.block.Block::getDefaultState, BlockState::new);
        this.setDefaultState(wrappedStateManager.getDefaultState());
        this.sandboxWrappedFactory = new StateFactoryImpl<>(wrappedStateManager, b -> (Block) b, s -> (org.sandboxpowered.api.state.BlockState) s);
        ((SandboxInternal.StateFactory<Block, org.sandboxpowered.api.state.BlockState>) wrappedStateManager).setSboxFactory(sandboxWrappedFactory);
        if (this.block instanceof BaseBlock)
            ((BaseBlock) this.block).setStateFactory(sandboxWrappedFactory);
        setDefaultState(WrappingUtil.convert(this.block.getBaseState()));
    }

    public static SandboxInternal.IBlockWrapper create(Block block) {
        if (block instanceof org.sandboxpowered.api.block.FluidBlock)
            return new WithFluid((FlowableFluid) WrappingUtil.convert(((org.sandboxpowered.api.block.FluidBlock) block).getFluid()), block);
        if (block instanceof FluidContainer) {
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

    private static ActionResult staticOnUse(org.sandboxpowered.api.state.BlockState state, org.sandboxpowered.api.world.World world, Position pos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult, Block block) {
        @SuppressWarnings("ConstantConditions") InteractionResult result = block.onBlockUsed(
                world,
                pos,
                state,
                player,
                hand == Hand.MAIN_HAND ? org.sandboxpowered.api.entity.player.Hand.MAIN_HAND : org.sandboxpowered.api.entity.player.Hand.OFF_HAND,
                WrappingUtil.convert(blockHitResult.getSide()),
                (Vec3f) (Object) new Vector3f(blockHitResult.getPos())
        );
        return WrappingUtil.convert(result);
    }

    @Override
    public ItemStack getPickStack(BlockView blockView, BlockPos blockPos, BlockState blockState) {
        return WrappingUtil.convert(this.block.getPickStack(
                WrappingUtil.convert(blockView),
                WrappingUtil.convert(blockPos),
                WrappingUtil.convert(blockState)
        ));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        return WrappingUtil.convert(block.getShape(
                WrappingUtil.convert(blockView),
                WrappingUtil.convert(blockPos),
                WrappingUtil.convert(blockState)
        ));
    }

    @Override
    public StateManager<net.minecraft.block.Block, BlockState> getStateManager() {
        if (wrappedStateManager == null)
            return super.getStateManager();
        return wrappedStateManager;
    }

    @Override
    protected void appendProperties(StateManager.Builder<net.minecraft.block.Block, BlockState> stateBuilder) {
        super.appendProperties(stateBuilder);
        if (block instanceof BaseBlock)
            ((BaseBlock) block).appendProperties(((SandboxInternal.StateFactoryBuilder<Block, org.sandboxpowered.api.state.BlockState>) stateBuilder).getSboxBuilder());
    }

    @Override
    public Block getSandboxBlock() {
        return block;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, net.minecraft.entity.player.PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        return staticOnUse((org.sandboxpowered.api.state.BlockState) state, (org.sandboxpowered.api.world.World) world, (Position) pos, (PlayerEntity) player, hand, hitResult, block);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return WrappingUtil.convert(block.getStateForPlacement(
                WrappingUtil.convert(context.getWorld()),
                WrappingUtil.convert(context.getBlockPos()),
                WrappingUtil.convert(context.getPlayer()),
                context.getHand() == Hand.MAIN_HAND ? org.sandboxpowered.api.entity.player.Hand.MAIN_HAND : org.sandboxpowered.api.entity.player.Hand.OFF_HAND,
                WrappingUtil.convert(context.getStack()),
                WrappingUtil.convert(context.getSide()),
                WrappingUtil.convert(context.getHitPos())
        ));
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, net.minecraft.entity.player.PlayerEntity player) {
        block.onBlockClicked(
                WrappingUtil.convert(world),
                WrappingUtil.convert(pos),
                WrappingUtil.convert(state),
                WrappingUtil.convert(player)
        );
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, net.minecraft.item.ItemStack stack) {
        block.onBlockPlaced(
                WrappingUtil.convert(world),
                WrappingUtil.convert(pos),
                WrappingUtil.convert(state),
                WrappingUtil.convert(entity),
                WrappingUtil.convert(stack)
        );
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        block.onBlockBroken(
                (org.sandboxpowered.api.world.World) world,
                (Position) pos,
                (org.sandboxpowered.api.state.BlockState) state
        );
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState otherState, WorldAccess world, BlockPos pos, BlockPos otherPos) {
        return WrappingUtil.convert(block.updateOnNeighborChanged(
                (org.sandboxpowered.api.state.BlockState) state,
                WrappingUtil.convert(direction),
                (org.sandboxpowered.api.state.BlockState) otherState,
                (org.sandboxpowered.api.world.World) world, (Position) pos,
                (Position) otherPos
        ));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return WrappingUtil.convert(block.rotate(
                (org.sandboxpowered.api.state.BlockState) state,
                WrappingUtil.convert(rotation)
        ));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return WrappingUtil.convert(block.mirror(
                (org.sandboxpowered.api.state.BlockState) state,
                WrappingUtil.convert(mirror)
        ));
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return block.canReplace(
                WrappingUtil.convert(context.getWorld()),
                WrappingUtil.convert(context.getBlockPos()),
                WrappingUtil.convert(state),
                WrappingUtil.convert(context.getPlayer()),
                context.getHand() == Hand.MAIN_HAND ? org.sandboxpowered.api.entity.player.Hand.MAIN_HAND : org.sandboxpowered.api.entity.player.Hand.OFF_HAND,
                WrappingUtil.convert(context.getStack()),
                WrappingUtil.convert(context.getSide()),
                WrappingUtil.convert(context.getHitPos())
        );
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, net.minecraft.entity.Entity entity) {
        block.onEntityWalk(
                WrappingUtil.convert(world),
                WrappingUtil.convert(pos),
                WrappingUtil.convert(entity)
        );
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return WrappingUtil.convert(block.getPistonInteraction((org.sandboxpowered.api.state.BlockState) state));
    }

    @Override
    public boolean canMobSpawnInside() {
        return block.canEntitySpawnWithin(block.getBaseState());
    }

    public static class WithFluid extends FluidBlock implements SandboxInternal.IBlockWrapper {
        private final Block block;

        public WithFluid(FlowableFluid basefluid, Block block) {
            super(basefluid, WrappingUtil.convert(block.getSettings()));
            this.block = block;
            if (this.block instanceof BaseBlock)
                ((BaseBlock) this.block).setStateFactory(((SandboxInternal.StateFactoryHolder<Block, org.sandboxpowered.api.state.BlockState>) this).getSandboxStateFactory());
        }

        @Override
        protected void appendProperties(StateManager.Builder<net.minecraft.block.Block, BlockState> stateBuilder) {
            super.appendProperties(stateBuilder);
            if (block instanceof org.sandboxpowered.api.block.FluidBlock)
                ((BaseBlock) block).appendProperties(((SandboxInternal.StateFactoryBuilder<Block, org.sandboxpowered.api.state.BlockState>) stateBuilder).getSboxBuilder());
        }

        @Override
        public Block getSandboxBlock() {
            return block;
        }

        @Override
        public ActionResult onUse(BlockState state, World world, BlockPos pos, net.minecraft.entity.player.PlayerEntity player, Hand hand, BlockHitResult hitResult) {
            return staticOnUse((org.sandboxpowered.api.state.BlockState) state, (org.sandboxpowered.api.world.World) world, (Position) pos, (PlayerEntity) player, hand, hitResult, block);
        }

        @Override
        public ItemStack getPickStack(BlockView blockView, BlockPos blockPos, BlockState blockState) {
            return WrappingUtil.convert(this.block.getPickStack(
                    WrappingUtil.convert(blockView),
                    WrappingUtil.convert(blockPos),
                    WrappingUtil.convert(blockState)
            ));
        }

        @Override
        public void onBlockBreakStart(BlockState state, World world, BlockPos pos, net.minecraft.entity.player.PlayerEntity player) {
            block.onBlockClicked(
                    (org.sandboxpowered.api.world.World) world,
                    (Position) pos,
                    (org.sandboxpowered.api.state.BlockState) state,
                    (PlayerEntity) player
            );
        }

        @Override
        public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity livingentity, net.minecraft.item.ItemStack stack) {
            block.onBlockPlaced(
                    (org.sandboxpowered.api.world.World) world,
                    (Position) pos,
                    (org.sandboxpowered.api.state.BlockState) state,
                    (Entity) livingentity,
                    WrappingUtil.convert(stack)
            );
        }

        @Override
        public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
            block.onBlockBroken(
                    (org.sandboxpowered.api.world.World) world,
                    (Position) pos,
                    (org.sandboxpowered.api.state.BlockState) state
            );
        }

        @Override
        public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState otherState, WorldAccess world, BlockPos pos, BlockPos otherPos) {
            return WrappingUtil.convert(block.updateOnNeighborChanged(
                    (org.sandboxpowered.api.state.BlockState) state, WrappingUtil.convert(direction), (org.sandboxpowered.api.state.BlockState) otherState, (org.sandboxpowered.api.world.World) world,
                    (Position) pos,
                    (Position) otherPos
            ));
        }

        @Override
        public BlockState rotate(BlockState state, BlockRotation rotation) {
            return WrappingUtil.convert(block.rotate(
                    (org.sandboxpowered.api.state.BlockState) state,
                    WrappingUtil.convert(rotation)
            ));
        }

        @Override
        public BlockState mirror(BlockState state, BlockMirror mirror) {
            return WrappingUtil.convert(block.mirror(
                    (org.sandboxpowered.api.state.BlockState) state,
                    WrappingUtil.convert(mirror)
            ));
        }

        @Override
        public boolean canReplace(BlockState state, ItemPlacementContext context) {
            return block.canReplace(
                    WrappingUtil.convert(context.getWorld()),
                    WrappingUtil.convert(context.getBlockPos()),
                    WrappingUtil.convert(state),
                    WrappingUtil.convert(context.getPlayer()),
                    context.getHand() == Hand.MAIN_HAND ? org.sandboxpowered.api.entity.player.Hand.MAIN_HAND : org.sandboxpowered.api.entity.player.Hand.OFF_HAND,
                    WrappingUtil.convert(context.getStack()),
                    WrappingUtil.convert(context.getSide()),
                    WrappingUtil.convert(context.getHitPos())
            );
        }

        @Override
        public void onSteppedOn(World world, BlockPos pos, net.minecraft.entity.Entity entity) {
            block.onEntityWalk(
                    (org.sandboxpowered.api.world.World) world,
                    (Position) pos,
                    WrappingUtil.convert(entity)
            );
        }

        @Override
        public PistonBehavior getPistonBehavior(BlockState state) {
            return WrappingUtil.convert(block.getPistonInteraction((org.sandboxpowered.api.state.BlockState) state));
        }

        @Override
        public boolean canMobSpawnInside() {
            return block.canEntitySpawnWithin(block.getBaseState());
        }
    }

    public static class WithBlockEntity extends BlockWrapper implements BlockEntityProvider {
        public WithBlockEntity(Block block) {
            super(block);
        }

        @Nullable
        @Override
        public BlockEntity createBlockEntity(BlockView var1) {
            return WrappingUtil.convert(getSandboxBlock().createBlockEntity((WorldReader) var1));
        }
    }

    public static class WithWaterloggable extends BlockWrapper implements Waterloggable {
        private final FluidContainer container;

        public WithWaterloggable(Block block) {
            super(block);
            this.container = (FluidContainer) block;
        }

        public FluidContainer getContainer() {
            return container;
        }

        @Override
        public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
            return ((org.sandboxpowered.api.state.BlockState) state).getComponent(
                    WrappingUtil.convert(world),
                    (Position) pos,
                    Components.FLUID_COMPONENT
            ).map(tank ->
                    tank.insert(FluidStack.of(WrappingUtil.convert(fluid), 1000), true).isEmpty()
            ).orElse(false);
        }

        @Override
        public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
            return ((org.sandboxpowered.api.state.BlockState) state).getComponent(
                    WrappingUtil.convert(world),
                    (Position) pos,
                    Components.FLUID_COMPONENT
            ).map(tank -> {
                FluidStack filled = FluidStack.of(WrappingUtil.convert(fluidState.getFluid()), 1000);
                FluidStack ret = tank.insert(filled, true);
                if (ret.isEmpty()) {
                    tank.insert(filled);
                    return true;
                } else {
                    return false;
                }
            }).orElse(false);
        }

        @Override
        public Fluid tryDrainFluid(WorldAccess world, BlockPos pos, BlockState state) {
            return WrappingUtil.convert(
                    ((org.sandboxpowered.api.state.BlockState) state).getComponent(
                            WrappingUtil.convert(world),
                            (Position) pos,
                            Components.FLUID_COMPONENT
                    ).map(tank ->
                            tank.extract(1000).getFluid()
                    ).orElse(Fluids.EMPTY.get())
            );
        }
    }

    public static class WithWaterloggableBlockEntity extends BlockWrapper.WithWaterloggable implements BlockEntityProvider {
        public WithWaterloggableBlockEntity(Block block) {
            super(block);
        }

        @Nullable
        @Override
        public BlockEntity createBlockEntity(BlockView var1) {
            return WrappingUtil.convert(getSandboxBlock().createBlockEntity((WorldReader) var1));
        }
    }
}