package org.sandboxpowered.sandbox.fabric.util.wrapper;

import org.sandboxpowered.sandbox.api.SandboxInternal;
import org.sandboxpowered.sandbox.api.fluid.BaseFluid;
import org.sandboxpowered.sandbox.api.util.Mono;
import org.sandboxpowered.sandbox.api.util.math.Position;
import org.sandboxpowered.sandbox.api.world.WorldReader;
import org.sandboxpowered.sandbox.fabric.util.ReflectionHelper;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.FluidStateImpl;
import net.minecraft.item.Item;
import net.minecraft.state.StateFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.ViewableWorld;

import java.lang.reflect.Field;

public class FluidWrapper extends net.minecraft.fluid.BaseFluid {
    public static Field whatever;

    public BaseFluid fluid;

    public FluidWrapper(BaseFluid fluid) {
        super();
        this.fluid = fluid;
        StateFactory.Builder<net.minecraft.fluid.Fluid, FluidState> stateFactory$Builder_1 = new StateFactory.Builder<>(this);
        this.appendProperties(stateFactory$Builder_1);
        try {
            ReflectionHelper.setPrivateField(net.minecraft.fluid.Fluid.class, this, "stateFactory", stateFactory$Builder_1.build(FluidStateImpl::new));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.setDefaultState((FluidState) this.stateFactory.getDefaultState());
    }

    public static FluidWrapper create(BaseFluid fluid) {
        return new FluidWrapper(fluid);
    }

    @Override
    protected void appendProperties(StateFactory.Builder<net.minecraft.fluid.Fluid, FluidState> stateFactory$Builder_1) {
        super.appendProperties(stateFactory$Builder_1);
        if (fluid != null)
            fluid.appendProperties(((SandboxInternal.StateFactoryBuilder) stateFactory$Builder_1).getSboxBuilder());
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
    protected void beforeBreakingBlock(IWorld iWorld_1, BlockPos blockPos_1, BlockState blockState_1) {
        BlockEntity blockEntity_1 = blockState_1.getBlock().hasBlockEntity() ? iWorld_1.getBlockEntity(blockPos_1) : null;
        Block.dropStacks(blockState_1, iWorld_1.getWorld(), blockPos_1, blockEntity_1);
    }

    @Override
    public Vec3d getVelocity(BlockView blockView_1, BlockPos blockPos_1, FluidState fluidState_1) {
        Mono<org.sandboxpowered.sandbox.api.util.math.Vec3d> mono = fluid.getVelocity(
                (WorldReader) blockView_1,
                (Position) blockPos_1,
                (org.sandboxpowered.sandbox.api.state.FluidState) fluidState_1
        );
        return mono.map(WrappingUtil::convert).orElseGet(() -> super.getVelocity(blockView_1, blockPos_1, fluidState_1));
    }

    @Override
    protected int method_15733(ViewableWorld var1) {
        return 4;
    }

    @Override
    protected int getLevelDecreasePerBlock(ViewableWorld var1) {
        return 1;
    }

    @Override
    protected BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public Item getBucketItem() {
        return WrappingUtil.convert(fluid.asItem());
    }

    @Override
    protected boolean method_15777(FluidState var1, BlockView var2, BlockPos var3, net.minecraft.fluid.Fluid var4, Direction var5) {
        return var5 == Direction.DOWN && !var4.matchesType(this);
    }

    @Override
    public int getTickRate(ViewableWorld var1) {
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
        return (BlockState) fluid.asBlockState((org.sandboxpowered.sandbox.api.state.FluidState) var1);
    }

    @Override
    public boolean isStill(FluidState var1) {
        return fluid.isStill((org.sandboxpowered.sandbox.api.state.FluidState) var1);
    }

    @Override
    public int getLevel(FluidState var1) {
        return isStill(var1) ? 8 : var1.get(LEVEL);
    }
}