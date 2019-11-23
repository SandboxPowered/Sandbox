package org.sandboxpowered.sandbox.fabric.mixin.impl.state;

import net.minecraft.state.AbstractState;
import net.minecraft.state.State;
import org.sandboxpowered.sandbox.api.state.Property;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(State.class)
@Implements(@Interface(iface = org.sandboxpowered.sandbox.api.state.PropertyContainer.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public interface MixinState {
    @Shadow
    public abstract <T extends Comparable<T>> T get(net.minecraft.state.property.Property<T> var1);

    @Shadow
    public abstract <T extends Comparable<T>, V extends T> Object with(net.minecraft.state.property.Property<T> var1, V var2);

    default Comparable sbx$get(Property property) {
        return get(WrappingUtil.convert(property));
    }

    default Object sbx$with(Property property, Comparable value) {
        return with(WrappingUtil.convert(property), value);
    }

    default boolean sbx$contains(Property property) {
        if (this instanceof AbstractState) {
            return ((AbstractState) this).contains(WrappingUtil.convert(property));
        }
        return false;
    }
}