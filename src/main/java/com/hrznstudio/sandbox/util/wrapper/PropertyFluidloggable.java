package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.api.SandboxInternal;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

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
            if (isFlowing(fluid)) {
                fluid.getStateFactory().getStates().forEach(state -> {
                    comparabilities.add(((SandboxInternal.FluidStateCompare) state).getComparability());
                });
            } else {
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
        String[] s = id.split("_");
        String path = StringUtils.join(ArrayUtils.remove(s, 0), '_');
        boolean flowing = isFlowing(path);
        Identifier identifier;
        int level = -1;
        if (flowing) {
            int index = path.lastIndexOf('_');
            if (index == -1) {
                flowing = false;
                identifier = new Identifier(s[0] + ":" + path);
            } else {
                identifier = new Identifier(s[0] + ":" + path.substring(0, index - 1));
                level = Integer.parseInt(path.substring(index + 1, path.length()));
            }
        } else {
            identifier = new Identifier(s[0] + ":" + path);
        }
        Fluid fluid = Registry.FLUID.get(identifier);
        if (flowing) {
            if (fluid.getStateFactory().getProperty("fluidlogged") == null) // I dont understand it
                return Optional.ofNullable(((SandboxInternal.FluidStateCompare) fluid.getDefaultState()).getComparability());
            return Optional.ofNullable(((SandboxInternal.FluidStateCompare) fluid.getDefaultState().with(BaseFluid.LEVEL, level)).getComparability());
        } else {
            return Optional.ofNullable(((SandboxInternal.FluidStateCompare) fluid.getDefaultState()).getComparability());
        }
    }

    @Override
    public String getName(FluidComparability var1) {
        if (isFlowing(var1.getFluidState().getFluid()))
            return transform(Registry.FLUID.getId(var1.getFluidState().getFluid()), var1.getFluidState());
        return transform(Registry.FLUID.getId(var1.getFluidState().getFluid()));
    }

    public String transform(Identifier id, FluidState state) {
        return id.getNamespace() + "_" + id.getPath() + "_" + state.get(BaseFluid.LEVEL);
    }

    public String transform(Identifier id) {
        return id.getNamespace() + "_" + id.getPath();
    }

    public boolean isValid(Fluid fluid) {
        return fluid.isStill(fluid.getDefaultState()) || fluid == Fluids.EMPTY || isFlowing(fluid);
    }

    public boolean isFlowing(Fluid fluid) {
        return isFlowing(Registry.FLUID.getId(fluid).getPath());
    }

    public boolean isFlowing(String id) {
        return id.contains("flowing");
    }

    public Identifier transform(String id) {
        String[] s = id.split("_");
        String path = StringUtils.join(ArrayUtils.remove(s, 0));
        boolean flowing = isFlowing(path);
        if (flowing) {
            int index = path.lastIndexOf('_');
            return new Identifier(s[0] + ":" + path.substring(0, index));
        }
        return new Identifier(s[0] + ":" + path);
    }
}