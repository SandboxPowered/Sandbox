package org.sandboxpowered.sandbox.fabric.mixin.impl.util;

import org.sandboxpowered.api.state.Property;
import org.spongepowered.asm.mixin.*;

import java.util.Collection;
import java.util.Optional;

@Mixin(net.minecraft.state.property.Property.class)
@Implements(@Interface(iface = Property.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinProperty {
    @Shadow
    public abstract String getName();

    @Shadow
    public abstract Collection<Comparable> getValues();

    @Shadow
    public abstract String name(Comparable var1);

    @Shadow
    public abstract Class<Comparable> getType();

    @Shadow
    public abstract Optional<Comparable> parse(String var1);

    public String sbx$getName() {
        return getName();
    }

    public String sbx$getName(Comparable value) {
        return name(value);
    }

    public Collection<Comparable> sbx$getValues() {
        return getValues();
    }

    public Class<Comparable> sbx$getValueType() {
        return getType();
    }

    public Optional<Comparable> sbx$getValue(String name) {
        return parse(name);
    }
}