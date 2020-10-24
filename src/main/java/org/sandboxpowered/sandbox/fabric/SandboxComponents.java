package org.sandboxpowered.sandbox.fabric;

import org.sandboxpowered.api.component.Component;
import org.sandboxpowered.api.component.FluidContainer;
import org.sandboxpowered.api.component.Inventory;
import org.sandboxpowered.sandbox.fabric.util.exception.UnknownComponentException;

public class SandboxComponents {
    public static final Component<Inventory> INVENTORY_COMPONENT = new Component<>(Inventory.class);
    public static final Component<FluidContainer> FLUID_CONTAINER_COMPONENT = new Component<>(FluidContainer.class);

    private SandboxComponents() {
    }

    @SuppressWarnings("unchecked")
    public static <X> Component<X> getComponent(Class<X> xClass) {
        if (xClass == Inventory.class)
            return (Component<X>) INVENTORY_COMPONENT;
        if (xClass == FluidContainer.class)
            return (Component<X>) FLUID_CONTAINER_COMPONENT;
        throw new UnknownComponentException(xClass);
    }
}