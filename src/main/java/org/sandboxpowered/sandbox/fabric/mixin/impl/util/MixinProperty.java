package org.sandboxpowered.sandbox.fabric.mixin.impl.util;

import org.sandboxpowered.api.state.Property;
import org.spongepowered.asm.mixin.*;

import java.util.Collection;
import java.util.Optional;

@Mixin(net.minecraft.state.property.Property.class)
@Implements(@Interface(iface = Property.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
@SuppressWarnings("java:S100")
public abstract class MixinProperty<T extends Comparable<T>> {
    @Shadow
    public abstract String getName();

    @Shadow
    public abstract Collection<T> getValues();

    @Shadow
    public abstract String name(T var1);

    @Shadow
    public abstract Class<T> getType();

    @Shadow
    public abstract Optional<T> parse(String var1);

    public String sbx$getName() {
        return getName();
    }

    public String sbx$getName(T value) {
        return name(value);
    }

    public Collection<T> sbx$getValues() {
        return getValues();
    }

    public Class<T> sbx$getValueType() {
        return getType();
    }

    public Optional<T> sbx$getValue(String name) {
        return parse(name);
    }
}
