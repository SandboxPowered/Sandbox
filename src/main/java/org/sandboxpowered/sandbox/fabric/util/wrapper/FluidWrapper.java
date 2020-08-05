package org.sandboxpowered.sandbox.fabric.util.wrapper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.sandboxpowered.api.fluid.BaseFluid;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.WorldReader;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.ReflectionHelper;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

import java.lang.reflect.Field;
import java.util.Optional;

public class FluidWrapper extends net.minecraft.fluid.FlowableFluid {
    public static Field whatever;

    public BaseFluid fluid;

    public FluidWrapper(BaseFluid fluid) {
        super();
        this.fluid = fluid;
        StateManager.Builder<net.minecraft.fluid.Fluid, FluidState> stateFactory$Builder_1 = new StateManager.Builder<>(this);
        this.appendProperties(stateFactory$Builder_1);
        try {
            ReflectionHelper.setPrivateField(net.minecraft.fluid.Fluid.class, this, new String[]{"field_15905", "stateManager"}, stateFactory$Builder_1.build(Fluid::getDefaultState, FluidState::new));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        this.setDefaultState(this.getStateManager().getDefaultState());
    }

    public static FluidWrapper create(BaseFluid fluid) {
        return new FluidWrapper(fluid);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void appendProperties(StateManager.Builder<net.minecraft.fluid.Fluid, FluidState> stateFactory$Builder_1) {
        super.appendProperties(stateFactory$Builder_1);
        if (fluid != null)
            fluid.appendProperties(((SandboxInternal.StateFactoryBuilder<org.sandboxpowered.api.fluid.Fluid, org.sandboxpowered.api.state.FluidState>) stateFactory$Builder_1).getSboxBuilder());
    }

    @Override
    public net.minecraft.fluid.Fluid getFlowing() {
        return WrappingUtil.convert(fluid.asFlowing());
    }

    @Override
    public net.minecraft.fluid.Fluid getStill() {
        return WrappingUtil.convert(fluid.asStill());
    }

    @Override
    protected boolean isInfinite() {
        return fluid.isInfinite();
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess iWorld_1, BlockPos blockPos_1, BlockState blockState_1) {
        BlockEntity blockEntity_1 = blockState_1.getBlock().hasBlockEntity() ? iWorld_1.getBlockEntity(blockPos_1) : null;
        Block.dropStacks(blockState_1, iWorld_1.getWorld(), blockPos_1, blockEntity_1);
    }

    @Override
    public Vec3d getVelocity(BlockView blockView_1, BlockPos blockPos_1, FluidState fluidState_1) {
        Optional<org.sandboxpowered.api.util.math.Vec3d> mono = fluid.getVelocity(
                (WorldReader) blockView_1,
                (Position) blockPos_1,
                WrappingUtil.convert(fluidState_1)
        );
        return mono.map(WrappingUtil::convertToVec).orElseGet(() -> super.getVelocity(blockView_1, blockPos_1, fluidState_1));
    }

    @Override
    protected int getFlowSpeed(WorldView worldView) {
        return 4;
    }

    @Override
    protected int getLevelDecreasePerBlock(WorldView var1) {
        return 1;
    }

// TODO
//
//    @Override
//    protected RenderLayer getRenderLayer() {
//        return RenderLayer.TRANSLUCENT;
//    }

    @Override
    public Item getBucketItem() {
        return WrappingUtil.convert(fluid.asBucket());
    }

    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockView blockView, BlockPos blockPos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.matchesType(this);
    }

    @Override
    public int getTickRate(WorldView var1) {
        return fluid.getTickRate((WorldReader) var1);
    }

    @Override
    protected float getBlastResistance() {
        return 100;
    }

    @Override
    public boolean matchesType(net.minecraft.fluid.Fluid fluid_1) {
        return fluid.matches(WrappingUtil.convert(fluid_1));
    }

    @Override
    protected BlockState toBlockState(FluidState var1) {
        return (BlockState) fluid.asBlockState(WrappingUtil.convert(var1));
    }

    @Override
    public boolean isStill(FluidState var1) {
        return fluid.isStill(WrappingUtil.convert(var1));
    }

    @Override
    public int getLevel(FluidState var1) {
        return isStill(var1) ? 8 : var1.get(LEVEL);
    }
}