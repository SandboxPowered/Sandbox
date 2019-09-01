package com.hrznstudio.sandbox.mixin.impl.state;

import com.hrznstudio.sandbox.SandboxProperties;
import com.hrznstudio.sandbox.api.SandboxInternal;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.AbstractPropertyContainer;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractPropertyContainer.class)
public abstract class MixinAbstractPropertyContainer {
    @Shadow
    public abstract <T extends Comparable<T>> T get(Property<T> property_1);

    @Shadow
    public abstract <T extends Comparable<T>, V extends T> Object with(Property<T> property_1, V comparable_1);

    @Inject(method = "with", at = @At("HEAD"), cancellable = true)
    public <T extends Comparable<T>, V extends T> void with(Property<T> property_1, V comparable_1, CallbackInfoReturnable info) {
        if (property_1 == Properties.WATERLOGGED) {
            if (comparable_1 == Boolean.TRUE)
                info.setReturnValue(with(SandboxProperties.PROPERTY_FLUIDLOGGABLE, ((SandboxInternal.FluidStateCompare) Fluids.WATER.getDefaultState()).getComparability()));
            else
                info.setReturnValue(with(SandboxProperties.PROPERTY_FLUIDLOGGABLE, ((SandboxInternal.FluidStateCompare) Fluids.EMPTY.getDefaultState()).getComparability()));
        }
    }

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    public <T extends Comparable<T>, V extends T> void get(Property<T> property_1, CallbackInfoReturnable<V> info) {
        if (property_1 == Properties.WATERLOGGED) {
            if (get(SandboxProperties.PROPERTY_FLUIDLOGGABLE).getFluidState().getFluid() == Fluids.WATER)
                info.setReturnValue((V) Boolean.TRUE);
            else
                info.setReturnValue((V) Boolean.FALSE);
        }
    }
}