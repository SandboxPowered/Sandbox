package com.hrznstudio.sandbox.mixin.impl.fluid;

import com.hrznstudio.sandbox.api.SandboxInternal;
import com.hrznstudio.sandbox.api.fluid.IFluid;
import com.hrznstudio.sandbox.api.state.FluidState;
import com.hrznstudio.sandbox.api.state.StateFactory;
import com.hrznstudio.sandbox.util.wrapper.FluidComparability;
import com.hrznstudio.sandbox.util.wrapper.StateFactoryImpl;
import net.minecraft.fluid.Fluid;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Fluid.class)
@Implements(@Interface(iface = IFluid.class, prefix = "sbx$"))
@Unique
public abstract class MixinFluid {
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

    @Shadow
    public abstract net.minecraft.fluid.FluidState getDefaultState();

    public FluidState sbx$getBaseState() {
        return (FluidState) getDefaultState();
    }

    public StateFactory<IFluid, FluidState> sbx$getStateFactory() {
        return ((SandboxInternal.StateFactory) stateFactory).getSboxFactory();
    }
}