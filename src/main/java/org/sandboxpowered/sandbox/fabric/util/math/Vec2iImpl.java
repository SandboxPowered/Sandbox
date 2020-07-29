package org.sandboxpowered.sandbox.fabric.util.math;

import org.sandboxpowered.api.util.math.Vec2i;

public class Vec2iImpl implements Vec2i {
    private final int x;
    private final int y;

    public Vec2iImpl(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}