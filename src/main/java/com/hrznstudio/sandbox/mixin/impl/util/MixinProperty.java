package com.hrznstudio.sandbox.mixin.impl.util;

import com.hrznstudio.sandbox.api.block.state.Property;
import org.spongepowered.asm.mixin.*;

import java.util.Collection;
import java.util.Optional;

@Mixin(net.minecraft.state.property.Property.class)
@Implements(@Interface(iface = Property.class, prefix = "sbx$"))
@Unique
public abstract class MixinProperty {
    @Shadow
    public abstract String getName();

    @Shadow
    public abstract String getName(Comparable var1);

    @Shadow
    public abstract Collection<Comparable> getValues();

    @Shadow
    public abstract Class<Comparable> getValueType();

    @Shadow
    public abstract Optional<Comparable> getValue(String var1);

    public String sbx$getName() {
        return getName();
    }

    public String sbx$getName(Comparable value) {
        return getName(value);
    }

    public Collection<Comparable> sbx$getValues() {
        return getValues();
    }

    public Class<Comparable> sbx$getValueType() {
        return getValueType();
    }

    public Optional<Comparable> sbx$getValue(String name) {
        return getValue(name);
    }
}