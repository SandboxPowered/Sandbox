package com.hrznstudio.sandbox.mixin.impl.fluid;

import com.hrznstudio.sandbox.api.SandboxInternal;
import com.hrznstudio.sandbox.api.fluid.IFluid;
import com.hrznstudio.sandbox.api.item.IItem;
import com.hrznstudio.sandbox.api.state.BlockState;
import com.hrznstudio.sandbox.api.state.FluidState;
import com.hrznstudio.sandbox.api.state.StateFactory;
import com.hrznstudio.sandbox.util.WrappingUtil;
import com.hrznstudio.sandbox.util.wrapper.FluidComparability;
import com.hrznstudio.sandbox.util.wrapper.StateFactoryImpl;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Fluid.class)
@Implements(@Interface(iface = IFluid.class, prefix = "sbx$"))
@Unique
public abstract class MixinFluid implements SandboxInternal.StateFactoryHolder  {
    @Shadow
    @Final
    protected net.minecraft.state.StateFactory<Fluid, net.minecraft.fluid.FluidState> stateFactory;
    private com.hrznstudio.sandbox.api.state.StateFactory<IFluid, FluidState> sandboxFactory;
    private FluidComparability comparability;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void constructor(CallbackInfo info) {
        sandboxFactory = new StateFactoryImpl<>(this.stateFactory, b -> (IFluid) b, s -> (com.hrznstudio.sandbox.api.state.FluidState) s);
        ((SandboxInternal.StateFactory) this.stateFactory).setSboxFactory(sandboxFactory);
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
    public abstract Item getBucketItem();

    public FluidState sbx$getBaseState() {
        return (FluidState) getDefaultState();
    }

    public StateFactory<IFluid, FluidState> sbx$getStateFactory() {
        return ((SandboxInternal.StateFactory) stateFactory).getSboxFactory();
    }

    public boolean sbx$isStill(FluidState state) {
        return isStill((net.minecraft.fluid.FluidState) state);
    }


    public BlockState sbx$asBlockState(FluidState state) {
        return (BlockState) this.toBlockState((net.minecraft.fluid.FluidState) state);
    }

    public IFluid sbx$asStill() {
        if ((Object) this instanceof BaseFluid)
            return WrappingUtil.convert(((BaseFluid) (Object) this).getStill());
        return WrappingUtil.convert(Fluids.EMPTY);
    }


    public IFluid sbx$asFlowing() {
        if ((Object) this instanceof BaseFluid)
            return WrappingUtil.convert(((BaseFluid) (Object) this).getFlowing());
        return WrappingUtil.convert(Fluids.EMPTY);
    }

    public boolean sbx$isInfinite() {
        if ((Object) this instanceof BaseFluid)
            return ((SandboxInternal.BaseFluid) this).sandboxinfinite();
        return false;
    }

    public IItem sbx$asItem() {
        return WrappingUtil.convert(getBucketItem());
    }
}