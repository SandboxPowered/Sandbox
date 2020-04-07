package org.sandboxpowered.sandbox.fabric.mixin.impl.util;

import org.sandboxpowered.sandbox.api.state.Property;
import org.spongepowered.asm.mixin.*;

import java.util.Collection;
import java.util.Optional;

@Mixin(net.minecraft.state.property.Property.class)
@Implements(@Interface(iface = Property.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public interface MixinProperty {
    @Shadow
    String getName();

    @Shadow
    Collection<Comparable> getValues();

    @Shadow
    String name(Comparable var1);

    @Shadow
    Class<Comparable> getType();

    @Shadow
    Optional<Comparable> parse(String var1);

    default String sbx$getName() {
        return getName();
    }

    default String sbx$getName(Comparable value) {
        return name(value);
    }

    default Collection<Comparable> sbx$getValues() {
        return getValues();
    }

    default Class<Comparable> sbx$getValueType() {
        return getType();
    }

    default Optional<Comparable> sbx$getValue(String name) {
        return parse(name);
    }
}