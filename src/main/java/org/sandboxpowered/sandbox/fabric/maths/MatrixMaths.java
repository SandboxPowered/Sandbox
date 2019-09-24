package org.sandboxpowered.sandbox.fabric.maths;

public final class MatrixMaths {

    public static PointD addRotAroundAxis(double x, double y, double z, double axis) {

        Matrix rotationMatrix = Matrix.identity();
        rotationMatrix.rotZ(z);
        rotationMatrix.rotY(y);
        rotationMatrix.rotX(x);
        rotationMatrix.rotY(axis);

        return new PointD(Math.atan2(rotationMatrix.m21, rotationMatrix.m22),
                Math.atan2(-rotationMatrix.m20, Math.sqrt(rotationMatrix.m21 * rotationMatrix.m21 + rotationMatrix.m22 * rotationMatrix.m22)),
                Math.atan2(rotationMatrix.m10, rotationMatrix.m00));
    }

}
