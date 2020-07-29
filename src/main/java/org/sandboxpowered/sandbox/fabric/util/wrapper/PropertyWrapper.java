package org.sandboxpowered.sandbox.fabric.util.wrapper;

import net.minecraft.state.property.Property;

import java.util.Collection;
import java.util.Optional;

public class PropertyWrapper<T extends Comparable<T>> extends Property<T> {
    private final org.sandboxpowered.api.state.Property<T> property;

    public PropertyWrapper(org.sandboxpowered.api.state.Property<T> property) {
        super(property.getName(), property.getValueType());
        this.property = property;
    }

    @Override
    public Collection<T> getValues() {
        return property.getValues();
    }

    @Override
    public String name(T comparable) {
        return property.getName(comparable);
    }

    @Override
    public Optional<T> parse(String string) {
        return property.getValue(string);
    }
}