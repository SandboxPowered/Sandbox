package org.sandboxpowered.sandbox.fabric;

import org.sandboxpowered.api.component.Component;
import org.sandboxpowered.api.component.Inventory;

public class SandboxComponents {

    public static final Component<Inventory> INVENTORY_COMPONENT = new Component<>(Inventory.class);

    public static <X> Component<X> getComponent(Class<X> xClass) {
        if (xClass == Inventory.class)
            return (Component<X>) INVENTORY_COMPONENT;
        throw new RuntimeException("Unknown registry " + xClass);
    }
}