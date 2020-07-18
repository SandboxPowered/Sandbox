package org.sandboxpowered.sandbox.fabric.util;

import net.minecraft.state.property.Properties;
import org.sandboxpowered.api.state.Property;

public class PropertyUtil {
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> Property<T> get(String s) {
        if (s.equals("attached"))
            return (Property<T>) Properties.ATTACHED;
        if (s.equals("bottom"))
            return (Property<T>) Properties.BOTTOM;
        if (s.equals("conditional"))
            return (Property<T>) Properties.CONDITIONAL;
        if (s.equals("disarmed"))
            return (Property<T>) Properties.DISARMED;
        if (s.equals("drag"))
            return (Property<T>) Properties.DRAG;
        if (s.equals("enabled"))
            return (Property<T>) Properties.ENABLED;
        if (s.equals("extended"))
            return (Property<T>) Properties.EXTENDED;
        if (s.equals("eye"))
            return (Property<T>) Properties.EYE;
        if (s.equals("falling"))
            return (Property<T>) Properties.FALLING;
        if (s.equals("hanging"))
            return (Property<T>) Properties.HANGING;
        if (s.equals("has_bottle_0"))
            return (Property<T>) Properties.HAS_BOTTLE_0;
        if (s.equals("has_bottle_1"))
            return (Property<T>) Properties.HAS_BOTTLE_1;
        if (s.equals("has_bottle_2"))
            return (Property<T>) Properties.HAS_BOTTLE_2;
        if (s.equals("has_record"))
            return (Property<T>) Properties.HAS_RECORD;
        if (s.equals("has_book"))
            return (Property<T>) Properties.HAS_BOOK;
        if (s.equals("inverted"))
            return (Property<T>) Properties.INVERTED;
        if (s.equals("in_wall"))
            return (Property<T>) Properties.IN_WALL;
        if (s.equals("lit"))
            return (Property<T>) Properties.LIT;
        if (s.equals("locked"))
            return (Property<T>) Properties.LOCKED;
        if (s.equals("occupied"))
            return (Property<T>) Properties.OCCUPIED;
        if (s.equals("open"))
            return (Property<T>) Properties.OPEN;
        if (s.equals("persistent"))
            return (Property<T>) Properties.PERSISTENT;
        if (s.equals("powered"))
            return (Property<T>) Properties.POWERED;
        if (s.equals("short"))
            return (Property<T>) Properties.SHORT;
        if (s.equals("signal_fire"))
            return (Property<T>) Properties.SIGNAL_FIRE;
        if (s.equals("snowy"))
            return (Property<T>) Properties.SNOWY;
        if (s.equals("triggered"))
            return (Property<T>) Properties.TRIGGERED;
        if (s.equals("unstable"))
            return (Property<T>) Properties.UNSTABLE;
        if (s.equals("waterlogged"))
            return (Property<T>) Properties.WATERLOGGED;
        if (s.equals("fluidlevel"))
            return (Property<T>) Properties.LEVEL_1_8;
        if (s.equals("level_15"))
            return (Property<T>) Properties.LEVEL_15;
        return null;
    }
}
