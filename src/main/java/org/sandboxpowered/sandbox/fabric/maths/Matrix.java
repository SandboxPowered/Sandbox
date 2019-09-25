package org.sandboxpowered.sandbox.fabric.maths;

// Only 3x3 matrix for maths in this mod atm
public class Matrix {

    public double m00, m01, m02;
    public double m10, m11, m12;
    public double m20, m21, m22;

    public Matrix() {
    }

    public Matrix(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22) {
        setMatrix(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    public static Matrix identity() {
        return new Matrix(1, 0, 0,
                0, 1, 0,
                0, 0, 1);
    }

    public Matrix clone() {
        return new Matrix(this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22);
    }

    public void setMatrix(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
    }

    // Adds rotations onto the current matrix

    public void rotX(double x) {
        double sin = Math.sin(x);
        double cos = Math.cos(x);

        this.mul(1, 0, 0,
                0, cos, -sin,
                0, sin, cos);
    }

    public void rotY(double y) {
        double sin = Math.sin(y);
        double cos = Math.cos(y);

        this.mul(cos, 0, sin,
                0, 1, 0,
                -sin, 0, cos);
    }

    public void rotZ(double z) {
        double sin = Math.sin(z);
        double cos = Math.cos(z);

        this.mul(cos, -sin, 0,
                sin, cos, 0,
                0, 0, 1);
    }

    public void mul(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22) {
        // t stands for temp
        double t00, t01, t02,
                t10, t11, t12,
                t20, t21, t22;

        t00 = this.m00 * m00 + this.m01 * m10 + this.m02 * m20;
        t01 = this.m00 * m01 + this.m01 * m11 + this.m02 * m21;
        t02 = this.m00 * m02 + this.m01 * m12 + this.m02 * m22;

        t10 = this.m10 * m00 + this.m11 * m10 + this.m12 * m20;
        t11 = this.m10 * m01 + this.m11 * m11 + this.m12 * m21;
        t12 = this.m10 * m02 + this.m11 * m12 + this.m12 * m22;

        t20 = this.m20 * m00 + this.m21 * m10 + this.m22 * m20;
        t21 = this.m20 * m01 + this.m21 * m11 + this.m22 * m21;
        t22 = this.m20 * m02 + this.m21 * m12 + this.m22 * m22;

        this.setMatrix(t00, t01, t02,
                t10, t11, t12,
                t20, t21, t22);
    }

    public void mul(Matrix mat) {
        this.mul(mat.m00, mat.m01, mat.m02, mat.m10, mat.m11, mat.m12, mat.m20, mat.m21, mat.m22);
    }
}
