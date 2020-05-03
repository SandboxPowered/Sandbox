package org.sandboxpowered.sandbox.fabric.mixin.impl.fluid;

import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.sandboxpowered.api.fluid.Fluid;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.state.FluidState;
import org.sandboxpowered.api.state.StateFactory;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.util.math.Vec3d;
import org.sandboxpowered.api.world.WorldReader;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.sandboxpowered.sandbox.fabric.util.wrapper.StateFactoryImpl;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@SuppressWarnings("ConstantConditions")
@Mixin(net.minecraft.fluid.Fluid.class)
@Implements(@Interface(iface = Fluid.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinFluid implements SandboxInternal.StateFactoryHolder {
    @Shadow
    @Final
    protected net.minecraft.state.StateManager<net.minecraft.fluid.Fluid, net.minecraft.fluid.FluidState> stateManager;
    private StateFactory<Fluid, FluidState> sandboxFactory;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void constructor(CallbackInfo info) {
        sandboxFactory = new StateFactoryImpl<>(this.stateManager, Fluid.class::cast, FluidState.class::cast);
        ((SandboxInternal.StateFactory) this.stateManager).setSboxFactory(sandboxFactory);
    }

    @Override
    public StateFactory getSandboxStateFactory() {
        return sandboxFactory;
    }

    @Shadow
    public abstract net.minecraft.fluid.FluidState getDefaultState();

    @Shadow
    public abstract boolean isStill(net.minecraft.fluid.FluidState var1);

    @Shadow
    protected abstract net.minecraft.block.BlockState toBlockState(net.minecraft.fluid.FluidState var1);

    @Shadow
    public abstract net.minecraft.item.Item getBucketItem();

    @Shadow
    protected abstract net.minecraft.util.math.Vec3d getVelocity(BlockView var1, BlockPos var2, net.minecraft.fluid.FluidState var3);

    public FluidState sbx$getBaseState() {
        return (FluidState) getDefaultState();
    }

    @SuppressWarnings("unchecked")
    public StateFactory<Fluid, FluidState> sbx$getStateFactory() {
        return ((SandboxInternal.StateFactory) stateManager).getSboxFactory();
    }

    public boolean sbx$isStill(FluidState state) {
        return isStill((net.minecraft.fluid.FluidState) state);
    }


    public BlockState sbx$asBlockState(FluidState state) {
        return (BlockState) this.toBlockState((net.minecraft.fluid.FluidState) state);
    }

    public Fluid sbx$asStill() {
        if ((Object) this instanceof FlowableFluid)
            return WrappingUtil.convert(((FlowableFluid) (Object) this).getStill());
        return WrappingUtil.convert(Fluids.EMPTY);
    }


    public Fluid sbx$asFlowing() {
        if ((Object) this instanceof FlowableFluid)
            return WrappingUtil.convert(((FlowableFluid) (Object) this).getFlowing());
        return WrappingUtil.convert(Fluids.EMPTY);
    }

    public boolean sbx$isInfinite() {
        if ((Object) this instanceof FlowableFluid)
            return ((SandboxInternal.BaseFluid) this).sandboxinfinite();
        return false;
    }

    public Item sbx$asBucket() {
        return WrappingUtil.convert(getBucketItem());
    }

    public Optional<Vec3d> sbx$getVelocity(WorldReader world, Position position, FluidState state) {
        return Optional.of(getVelocity(
                (BlockView) world,
                (BlockPos) position,
                (net.minecraft.fluid.FluidState) state
        )).map(vec -> (Vec3d) vec);
    }
}