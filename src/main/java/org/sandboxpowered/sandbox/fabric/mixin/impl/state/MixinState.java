package org.sandboxpowered.sandbox.fabric.mixin.impl.state;

import net.minecraft.state.State;
import org.sandboxpowered.api.state.Property;
import org.sandboxpowered.api.state.PropertyContainer;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.sandboxpowered.sandbox.fabric.util.wrapper.EnumPropertyWrapper;
import org.spongepowered.asm.mixin.*;

@Mixin(State.class)
@Implements(@Interface(iface = PropertyContainer.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinState {
    @Shadow
    public abstract <T extends Comparable<T>> T get(net.minecraft.state.property.Property<T> var1);

    @Shadow
    public abstract <T extends Comparable<T>, V extends T> Object with(net.minecraft.state.property.Property<T> var1, V var2);

    @Shadow
    public abstract <T extends Comparable<T>> boolean contains(net.minecraft.state.property.Property<T> property);

    public <T extends Comparable<T>> T sbx$get(Property<T> property) {
        return get(WrappingUtil.convert(property));
    }

    public <T extends Comparable<T>> Object sbx$with(Property<T> property, T value) {
        if (property instanceof EnumPropertyWrapper) {
            value = (T) ((EnumPropertyWrapper) property).getS2VFunction().apply(value);
        }
        return with(WrappingUtil.convert(property), value);
    }

    public <T extends Comparable<T>> boolean sbx$contains(Property<T> property) {
        return this.contains(WrappingUtil.convert(property));
    }
}