package org.sandboxpowered.sandbox.fabric.mixin.impl.state;

import net.minecraft.state.State;
import org.sandboxpowered.api.state.PropertyContainer;
import org.sandboxpowered.api.state.Property;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(State.class)
@Implements(@Interface(iface = PropertyContainer.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public interface MixinState {
    @Shadow
    <T extends Comparable<T>> T get(net.minecraft.state.property.Property<T> var1);

    @Shadow
    <T extends Comparable<T>, V extends T> Object with(net.minecraft.state.property.Property<T> var1, V var2);

    @Shadow
    <T extends Comparable<T>> boolean contains(net.minecraft.state.property.Property<T> property);

    default <T extends Comparable<T>> T sbx$get(Property<T> property) {
        return get(WrappingUtil.convert(property));
    }

    default <T extends Comparable<T>> Object sbx$with(Property<T> property, T value) {
        return with(WrappingUtil.convert(property), value);
    }

    default <T extends Comparable<T>> boolean sbx$contains(Property<T> property) {
        return this.contains(WrappingUtil.convert(property));
    }
}