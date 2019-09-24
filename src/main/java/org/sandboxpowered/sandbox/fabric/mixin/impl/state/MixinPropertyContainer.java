package org.sandboxpowered.sandbox.fabric.mixin.impl.state;

import org.sandboxpowered.sandbox.api.state.Property;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import net.minecraft.state.AbstractPropertyContainer;
import net.minecraft.state.PropertyContainer;
import org.spongepowered.asm.mixin.*;

@Mixin(PropertyContainer.class)
@Implements(@Interface(iface = org.sandboxpowered.sandbox.api.state.PropertyContainer.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public interface MixinPropertyContainer {
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
        if (this instanceof AbstractPropertyContainer) {
            return ((AbstractPropertyContainer) this).contains(WrappingUtil.convert(property));
        }
        return false;
    }
}