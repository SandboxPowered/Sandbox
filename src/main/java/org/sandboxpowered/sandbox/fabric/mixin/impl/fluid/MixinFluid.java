package org.sandboxpowered.sandbox.fabric.mixin.impl.fluid;

import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.fluid.Fluid;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.state.BlockState;
import org.sandboxpowered.api.state.FluidState;
import org.sandboxpowered.api.state.StateFactory;
import org.sandboxpowered.api.util.Identity;
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

@Mixin(net.minecraft.fluid.Fluid.class)
@Implements(@Interface(iface = Fluid.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
@SuppressWarnings({"ConstantConditions", "java:S100", "java:S1610"})
public abstract class MixinFluid implements SandboxInternal.StateFactoryHolder<Fluid, FluidState> {
    @Shadow
    @Final
    protected net.minecraft.state.StateManager<net.minecraft.fluid.Fluid, net.minecraft.fluid.FluidState> stateManager;
    private StateFactory<Fluid, FluidState> sandboxFactory;
    private Identity identity;

    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("RETURN"))
    private void constructor(CallbackInfo info) {
        sandboxFactory = new StateFactoryImpl<>(this.stateManager, Fluid.class::cast, FluidState.class::cast);
        ((SandboxInternal.StateFactory<Fluid, FluidState>) this.stateManager).setSboxFactory(sandboxFactory);
    }

    @Override
    public StateFactory<Fluid, FluidState> getSandboxStateFactory() {
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
        return WrappingUtil.convert(getDefaultState());
    }

    @SuppressWarnings("unchecked")
    public StateFactory<Fluid, FluidState> sbx$getStateFactory() {
        return ((SandboxInternal.StateFactory<Fluid, FluidState>) stateManager).getSboxFactory();
    }

    public boolean sbx$isStill(FluidState state) {
        return isStill(WrappingUtil.convert(state));
    }

    public BlockState sbx$asBlockState(FluidState state) {
        return (BlockState) this.toBlockState(WrappingUtil.convert(state));
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
            return ((SandboxInternal.BaseFluid) this).sandboxInfinite();
        return false;
    }

    public Item sbx$asBucket() {
        return WrappingUtil.convert(getBucketItem());
    }

    public Optional<Vec3d> sbx$getVelocity(WorldReader world, Position position, FluidState state) {
        return Optional.of(getVelocity(
                (BlockView) world,
                (BlockPos) position,
                WrappingUtil.convert(state)
        )).map(vec -> (Vec3d) vec);
    }

    public Identity sbx$getIdentity() {
        if (this instanceof SandboxInternal.IFluidWrapper) {
            return ((SandboxInternal.IFluidWrapper) this).getSandboxFluid().getIdentity();
        }
        if (this.identity == null)
            this.identity = WrappingUtil.convert(Registry.FLUID.getId(WrappingUtil.cast(this, net.minecraft.fluid.Fluid.class)));
        return identity;
    }

    public Content<?> sbx$setIdentity(Identity identity) {
        if (this instanceof SandboxInternal.IFluidWrapper) {
            return ((SandboxInternal.IFluidWrapper) this).getSandboxFluid().setIdentity(identity);
        }
        throw new UnsupportedOperationException("Cannot set identity on content with existing identity");
    }
}
