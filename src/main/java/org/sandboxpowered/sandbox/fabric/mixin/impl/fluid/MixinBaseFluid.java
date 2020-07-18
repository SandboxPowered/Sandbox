package org.sandboxpowered.sandbox.fabric.mixin.impl.fluid;

import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FlowableFluid.class)
public abstract class MixinBaseFluid extends Fluid implements SandboxInternal.BaseFluid {
    @Shadow
    protected abstract boolean isInfinite();

    @Override
    public boolean sandboxInfinite() {
        return isInfinite();
    }
}