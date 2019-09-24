package org.sandboxpowered.sandbox.fabric.maths;

public class RotateF {

    public float x;

    public float y;

    public float z;

    public RotateF(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public RotateF() {
        this(0, 0, 0);
    }

    public RotateF add(RotateF r) {
        this.x += r.x;
        this.y += r.y;
        this.z += r.z;
        return this;
    }

    public RotateF sub(RotateF r) {
        this.x -= r.x;
        this.y -= r.y;
        this.z -= r.z;
        return this;
    }

    public RotateF clone() {
        return new RotateF(this.x, this.y, this.z);
    }

    public RotateF copy(RotateF rotation) {
        this.x = rotation.x;
        this.y = rotation.y;
        this.z = rotation.z;
        return this;
    }

    /**
     * Designed for if the stored value is storing a angle difference.
     */
    public void changeToShortestAngle() {
        this.x = this.shortestAngleTo(this.x);
        this.y = this.shortestAngleTo(this.y);
        this.z = this.shortestAngleTo(this.z);
    }

    /**
     * If its more than 180 then it reverses it to get the shortest
     *
     * @return
     */
    public float shortestAngleTo(float angle) {
        if (angle > Math.PI) {
            return (float) (-Math.PI * 2) + angle;
        } else if (angle < -Math.PI) {
            return (float) (Math.PI * 2) + angle;
        }
        return angle;
    }
}
