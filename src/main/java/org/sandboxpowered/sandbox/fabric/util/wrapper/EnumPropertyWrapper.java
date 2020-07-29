package org.sandboxpowered.sandbox.fabric.util.wrapper;


import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import org.sandboxpowered.api.state.Property;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumPropertyWrapper<S extends Enum<S>, V extends Enum<V> & StringIdentifiable> implements Property<S> {
    private final EnumProperty<V> enumProperty;
    private final Function<V, S> v2SFunction;
    private final Function<S, V> s2VFunction;
    private final Class<S> valueType;

    public EnumPropertyWrapper(EnumProperty<V> enumProperty, Function<V, S> v2SFunction, Function<S, V> s2VFunction, Class<S> valueType) {
        this.enumProperty = enumProperty;
        this.v2SFunction = v2SFunction;
        this.s2VFunction = s2VFunction;
        this.valueType = valueType;
    }

    public EnumProperty<V> getEnumProperty() {
        return enumProperty;
    }

    public Function<V, S> getV2SFunction() {
        return v2SFunction;
    }

    public Function<S, V> getS2VFunction() {
        return s2VFunction;
    }

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