package com.hrznstudio.sandbox.mixin.impl.util;

import com.hrznstudio.sandbox.api.state.Property;
import org.spongepowered.asm.mixin.*;

import java.util.Collection;
import java.util.Optional;

@Mixin(net.minecraft.state.property.Property.class)
@Implements(@Interface(iface = Property.class, prefix = "sbx$"))
@Unique
public abstract interface MixinProperty {
    @Shadow
    String getName();

    @Shadow
    String getName(Comparable var1);

    @Shadow
    Collection<Comparable> getValues();

    @Shadow
    Class<Comparable> getValueType();

    @Shadow
    Optional<Comparable> getValue(String var1);

    default String sbx$getName() {
        return getName();
    }

    default String sbx$getName(Comparable value) {
        return getName(value);
    }

    default Collection<Comparable> sbx$getValues() {
        return getValues();
    }

    default Class<Comparable> sbx$getValueType() {
        return getValueType();
    }

    default Optional<Comparable> sbx$getValue(String name) {
        return getValue(name);
    }
}