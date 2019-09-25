package org.sandboxpowered.sandbox.fabric.maths;

/**
 * Change the code to be like RotateF
 * Created by sekawh on 8/4/2015.
 */
public class PointF {

    public final float x;

    public final float y;

    public final float z;

    public PointF(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PointF() {
        this(0, 0, 0);
    }

    public PointF add(PointF v) {
        return new PointF(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    public PointF sub(PointF v) {
        return new PointF(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    public PointF multiply(float multi) {
        return new PointF(this.x * multi, this.y * multi, this.z * multi);
    }
}
