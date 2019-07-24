package com.hrznstudio.sandbox.ragdoll.parts.trackers;

import com.hrznstudio.sandbox.maths.PointD;
import com.hrznstudio.sandbox.maths.VectorMaths;
import com.hrznstudio.sandbox.ragdoll.parts.Triangle;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.model.Cuboid;

/**
 * Created by on 30/06/2016.
 *
 * @author sekwah41
 */
public class TrackerTriangle extends Tracker {

    protected final Triangle triangle;

    protected float lastRotationAxis = 0;

    protected float rotationAxis = 0;

    protected float rotationAxisDiff = 0;

    public TrackerTriangle(Cuboid part, Triangle triangle) {
        super(part);
        this.triangle = triangle;

        this.calcPosition();
    }

    public TrackerTriangle(Cuboid part, Triangle triangle, float rotateOffsetX, float rotateOffsetY, float rotateOffsetZ) {
        super(part, rotateOffsetX, rotateOffsetY, rotateOffsetZ);
        this.triangle = triangle;
    }

    @Override
    public void render(float partialTicks) {
        this.renderPart(partialTicks);
    }

    public void calcPosition() {
        // TODO Find out why convertToF is broken

        this.updateLastPos();

        PointD triangleDir = triangle.getDirection();

        PointD triangleNorm = triangle.getNormal();

        // TODO need to finish the rotation to the correct locaiton, though check the maths is howyou expected it first.

        this.rotation.y = basicRotation(triangleDir.x, triangleDir.z);

        this.rotation.x = (float) (Math.PI * 0.5) + basicRotation(-triangleDir.y, (float) Math.sqrt(triangleDir.x * triangleDir.x + triangleDir.z * triangleDir.z));

        // TODO calc rotation to keep tracked

        PointD angleDifference = VectorMaths.rotateOriginY(-this.rotation.y, triangleNorm);
        angleDifference = VectorMaths.rotateOriginX(-this.rotation.x, angleDifference);

        this.rotationAxis = basicRotation(angleDifference.x, angleDifference.z);

        this.updatePosition();

        this.updatePosDifference();

        // TODO figure out the rotation to get it to the right location.


        // Calculate what the vertex would be if it was just rotated to the direction, then find the angle between the two points.
        // Find a way to calculate if it should be positive or negative.

        // Or rotate the angle to the default and find it using the same way you find rotationX (will be a lot easier if it works)

        /*Matrix4f rotMatrix = new Matrix4f();
        rotMatrix.rotate(-rotationX, new Vector3f(1, 0, 0));
        rotMatrix.rotate(-rotationY, new Vector3f(0, 1, 0));*/

    }

    @Override
    protected void smoothRotation(float partialTicks) {
        super.smoothRotation(partialTicks);
        GlStateManager.rotatef((float) Math.toDegrees(this.lastRotationAxis + this.rotationAxisDiff * partialTicks), 0, 1, 0);
    }

    protected void updatePosition() {
        this.position = new PointD(triangle.points[0].posX, triangle.points[0].posY,
                triangle.points[0].posZ);
    }

    public float basicRotation(double axis1, double axis2) {
        return (float) Math.atan2(axis1, axis2);
    }

}
