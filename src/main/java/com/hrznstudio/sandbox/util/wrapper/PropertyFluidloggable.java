package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.api.SandboxInternal;
import com.hrznstudio.sandbox.util.Log;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;

//TODO: Make this not garbage
public class PropertyFluidloggable implements Property<FluidComparability> {
    @Override
    public String getName() {
        return "fluidlogged";
    }

    @Override
    public Collection<FluidComparability> getValues() {
        Collection<FluidComparability> comparabilities = new LinkedHashSet<>();
        for (Fluid fluid : Registry.FLUID) {
            if (fluid.isStill(fluid.getDefaultState()) || fluid == Fluids.EMPTY) {
                fluid.getStateFactory().getStates().forEach(state -> {
                    comparabilities.add(((SandboxInternal.FluidStateCompare) state).getComparability());
                });
            }
        }
        return comparabilities;
    }

    @Override
    public Class<FluidComparability> getValueType() {
        return FluidComparability.class;
    }

    @Override
    public Optional<FluidComparability> getValue(String id) {
        try {
            int index = id.lastIndexOf("_");
            if (index == -1)
                return Optional.empty();
            int length = Integer.parseInt(id.substring(index + 1));
            id = id.substring(0, index);
            String namespace = id.substring(0, length);
            String path = id.substring(length);
            Identifier identifier = new Identifier(namespace, path);
            Fluid fluid = Registry.FLUID.get(identifier);
            return Optional.ofNullable(((SandboxInternal.FluidStateCompare) fluid.getDefaultState()).getComparability());
        } catch (Exception e) { // Do this to prevent mass chunk corruption if something unexpected happens
            Log.error("An error occurred parsing fluids", e);
            return Optional.empty();
        }
    }

    @Override
    public String getName(FluidComparability var1) {
        return transform(Registry.FLUID.getId(var1.getFluidState().getFluid()));
    }

    public String transform(Identifier id) {
        return id.getNamespace() + id.getPath() + "_" + id.getNamespace().length();
    }

    public boolean isValid(Fluid fluid) {
        return fluid.isStill(fluid.getDefaultState()) || fluid == Fluids.EMPTY;
    }
}