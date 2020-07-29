package org.sandboxpowered.sandbox.fabric.util.wrapper;


import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import org.sandboxpowered.api.state.Property;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumPropertyWrapper<S extends Enum<S>, V extends Enum<V> & StringIdentifiable> implements Property<S> {
    private EnumProperty<V> enumProperty;
    private Function<V, S> v2SFunction;
    private Function<S, V> s2VFunction;
    private Class<S> valueType;

    @Override
    public String getName() {
        return enumProperty.getName();
    }

    @Override
    public String getName(S value) {
        return enumProperty.name(s2VFunction.apply(value));
    }

    @Override
    public Collection<S> getValues() {
        return enumProperty.getValues().stream().map(v2SFunction).collect(Collectors.toSet());
    }

    @Override
    public Class<S> getValueType() {
        return valueType;
    }

    @Override
    public Optional<S> getValue(String name) {
        return enumProperty.parse(name).map(v2SFunction);
    }
}