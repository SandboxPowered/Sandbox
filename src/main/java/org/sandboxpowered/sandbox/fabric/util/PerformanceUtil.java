package org.sandboxpowered.sandbox.fabric.util;

import net.minecraft.util.math.Direction;

public class PerformanceUtil {
    private static final Direction[] DIRECTIONS = Direction.values();

    private PerformanceUtil() {
    }

    public static Direction[] getDirectionArray() {
        return DIRECTIONS;
    }
}
