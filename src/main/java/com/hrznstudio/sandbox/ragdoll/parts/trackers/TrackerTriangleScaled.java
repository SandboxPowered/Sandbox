package com.hrznstudio.sandbox.ragdoll.parts.trackers;

import com.hrznstudio.sandbox.maths.PointD;
import com.hrznstudio.sandbox.ragdoll.parts.Triangle;
import net.minecraft.client.model.Cuboid;

/**
 * Created by on 30/06/2016.
 *
 * @author sekwah41
 */
public class TrackerTriangleScaled extends TrackerTriangle {

    private final float scale;
    private final float scaleInvert;

    public TrackerTriangleScaled(Cuboid part, Triangle triangle, float scale) {
        super(part, triangle);
        this.scale = scale;
        this.scaleInvert = 1f / scale;
    }

    public TrackerTriangleScaled(Cuboid part, Triangle triangle, float rotateOffsetX, float rotateOffsetY,
                                 float rotateOffsetZ, float scale) {
        super(part, triangle, rotateOffsetX, rotateOffsetY, rotateOffsetZ);
        this.scale = scale;
        this.scaleInvert = 1f / scale;
    }

    public void render(float partialTicks) {
        this.renderPart(partialTicks, 0.0625f * this.scale);
    }

    @Override
    protected void updatePosition() {
        this.position = new PointD(triangle.points[0].posX * this.scaleInvert, triangle.points[0].posY * this.scaleInvert,
                triangle.points[0].posZ * this.scaleInvert);
    }

}
