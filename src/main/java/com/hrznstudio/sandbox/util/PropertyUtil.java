package com.hrznstudio.sandbox.util;

import com.hrznstudio.sandbox.api.state.Property;
import net.minecraft.state.property.Properties;

public class PropertyUtil {
    public static Property get(String s) {
        if (s.equals("attached"))
            return (Property) Properties.ATTACHED;
        if (s.equals("bottom"))
            return (Property) Properties.BOTTOM;
        if (s.equals("conditional"))
            return (Property) Properties.CONDITIONAL;
        if (s.equals("disarmed"))
            return (Property) Properties.DISARMED;
        if (s.equals("drag"))
            return (Property) Properties.DRAG;
        if (s.equals("enabled"))
            return (Property) Properties.ENABLED;
        if (s.equals("extended"))
            return (Property) Properties.EXTENDED;
        if (s.equals("eye"))
            return (Property) Properties.EYE;
        if (s.equals("falling"))
            return (Property) Properties.FALLING;
        if (s.equals("hanging"))
            return (Property) Properties.HANGING;
        if (s.equals("has_bottle_0"))
            return (Property) Properties.HAS_BOTTLE_0;
        if (s.equals("has_bottle_1"))
            return (Property) Properties.HAS_BOTTLE_1;
        if (s.equals("has_bottle_2"))
            return (Property) Properties.HAS_BOTTLE_2;
        if (s.equals("has_record"))
            return (Property) Properties.HAS_RECORD;
        if (s.equals("has_book"))
            return (Property) Properties.HAS_BOOK;
        if (s.equals("inverted"))
            return (Property) Properties.INVERTED;
        if (s.equals("in_wall"))
            return (Property) Properties.IN_WALL;
        if (s.equals("lit"))
            return (Property) Properties.LIT;
        if (s.equals("locked"))
            return (Property) Properties.LOCKED;
        if (s.equals("occupied"))
            return (Property) Properties.OCCUPIED;
        if (s.equals("open"))
            return (Property) Properties.OPEN;
        if (s.equals("persistent"))
            return (Property) Properties.PERSISTENT;
        if (s.equals("powered"))
            return (Property) Properties.POWERED;
        if (s.equals("short"))
            return (Property) Properties.SHORT;
        if (s.equals("signal_fire"))
            return (Property) Properties.SIGNAL_FIRE;
        if (s.equals("snowy"))
            return (Property) Properties.SNOWY;
        if (s.equals("triggered"))
            return (Property) Properties.TRIGGERED;
        if (s.equals("unstable"))
            return (Property) Properties.UNSTABLE;
        if (s.equals("waterlogged"))
            return (Property) Properties.WATERLOGGED;
        if (s.equals("fluidlevel"))
            return (Property) Properties.LEVEL_1_8;
        if (s.equals("level_15"))
            return (Property) Properties.LEVEL_15;
        return null;
    }
}