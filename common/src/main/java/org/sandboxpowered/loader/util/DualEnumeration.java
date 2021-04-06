package org.sandboxpowered.loader.util;

import java.util.Enumeration;

public class DualEnumeration<T> implements Enumeration<T> {
    private Enumeration<T> first, second, selected;

    public DualEnumeration(Enumeration<T> first, Enumeration<T> second) {
        this.first = first;
        this.second = second;
        this.selected = first;
    }

    @Override
    public boolean hasMoreElements() {
        return selected != null && (selected.hasMoreElements() || selected == first && second.hasMoreElements());
    }

    @Override
    public T nextElement() {
        if (selected == null) {
            return null;
        }

        if (!selected.hasMoreElements()) {
            if (selected == first) {
                selected = second;
            } else {
                selected = null;
                return null;
            }
        }

        return selected.nextElement();
    }
}
