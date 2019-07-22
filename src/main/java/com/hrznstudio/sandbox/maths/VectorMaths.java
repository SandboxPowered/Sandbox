package com.hrznstudio.sandbox.maths;

/**
 * TODO allow the code to apply multiple matricies then apply the rotation. (Make another class for that)
 *
 * Created by sekwah41 on 07/07/2017.
 */
public final class VectorMaths {

    public static PointD normalize(PointD p1, PointD p2) {
        double norm = Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) + Math.pow(p1.z - p2.z, 2));

        return normalize(p1, p2, norm);
    }

    public static PointD normalize(PointD p1, PointD p2, double precalcNorm) {
        double norm = 1.0d/precalcNorm;

        return new PointD((p1.x - p2.x) * norm,
                (p1.y - p2.y) * norm, (p1.z - p2.z) * norm);
    }

    public static PointD getTriangleNormal(PointD p1, PointD p2, PointD p3) {
        PointD v1 = p1.sub(p2);
        PointD v2 = p1.sub(p3);
        double normX = v1.y * v2.z - v1.z * v2.y;
        double normY = v1.z * v2.x - v1.x * v2.z;
        double normZ = v1.x * v2.y - v1.y * v2.x;

        return new PointD(normX, normY, normZ);
    }

    /**
     * Rotate the point anticlockwise around the origin
     * @param angle
     * @param p
     * @return
     */
    public static PointD rotateOriginX(double angle, PointD p) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        double y = p.y * cos + p.z * -sin;
        double z = p.y * sin + p.z * cos;

        return new PointD(p.x, y, z);
    }

    /**
     * Rotate the point anticlockwise around the origin
     * @param angle
     * @param p
     * @return
     */
    public static PointD rotateOriginY(double angle, PointD p) {

        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        
        double x = p.x * cos + p.z * sin;
        double z = p.x * -sin + p.z * cos;

        //return new PointD(p.x, p.y, p.z);
        return new PointD(x, p.y, z);
    }

    /**
     * Rotate the point anticlockwise around the origin
     * @param angle
     * @param p
     * @return
     */
    public static PointD rotateOriginZ(double angle, PointD p) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        double x = p.x * cos + p.y * -sin;
        double y = p.x * sin + p.y * cos;

        return new PointD(x, y, p.z);
    }

}
