package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.api.state.FluidProperty;
import net.minecraft.util.registry.Registry;

public class FluidComparability implements Comparable<FluidComparability> {
    private final net.minecraft.fluid.FluidState fluid;

    public FluidComparability(net.minecraft.fluid.FluidState fluid) {
        this.fluid = fluid;
    }

    public net.minecraft.fluid.FluidState getFluidState() {
        return fluid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FluidComparability that = (FluidComparability) o;

        return fluid != null ? fluid.equals(that.fluid) : that.fluid == null;
    }

    @Override
    public int hashCode() {
        return fluid != null ? fluid.hashCode() : 0;
    }

    @Override
    public int compareTo(FluidComparability o) {
        return Registry.FLUID.getId(fluid.getFluid()).toString().compareTo(Registry.FLUID.getId(o.fluid.getFluid()).toString());
    }

    @Override
    public String toString() {
        return "FluidComparability{" +
                "fluid=" + fluid +
                '}';
    }
}