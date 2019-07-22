package com.hrznstudio.sandbox.maths;

/**
 * Change the code to be like RotateF
 * Created by sekawh on 8/4/2015.
 */
public class PointD {

    public final double x;

    public final double y;

    public final double z;

    public PointD(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PointD() {
        this(0,0,0);
    }

    public PointD add(PointD v) {
        return new PointD(this.x + v.x, this.y + v.y, this.z + v.z);
    }

    public PointD sub(PointD v) {
        return new PointD(this.x - v.x, this.y - v.y, this.z - v.z);
    }

    public PointD multiply(double multi) {
        return new PointD(this.x * multi, this.y * multi, this.z * multi);
    }

    public PointF convertToF() {
        return new PointF((float) x, (float) y, (float)z);
    }
}
