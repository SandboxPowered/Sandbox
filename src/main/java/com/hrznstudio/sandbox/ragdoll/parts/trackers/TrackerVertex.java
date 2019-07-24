package com.hrznstudio.sandbox.ragdoll.parts.trackers;

import com.hrznstudio.sandbox.maths.PointD;
import com.hrznstudio.sandbox.maths.PointF;
import com.hrznstudio.sandbox.ragdoll.parts.SkeletonPoint;
import net.minecraft.client.model.Cuboid;

/**
 * Created by on 30/06/2016.
 * <p>
 * TODO possibly recode to use a constraint rather than set points
 *
 * @author sekwah41
 */
public class TrackerVertex extends Tracker {

    protected final SkeletonPoint anchor;

    protected final SkeletonPoint pointsTo;

    public TrackerVertex(Cuboid part, SkeletonPoint anchor, SkeletonPoint pointsTo) {
        super(part);
        this.anchor = anchor;
        this.pointsTo = pointsTo;

        // Resets the data so it doesnt get a sliding effect from 0 of every location.


    }

    @Override
    public void render(float partialTicks) {
        this.renderPart(partialTicks);
    }

    @Override
    public void calcPosition() {

        this.updateLastPos();

        PointF constraintVert = new PointF((float) (pointsTo.posX - anchor.posX), (float) (pointsTo.posY - anchor.posY),
                (float) (pointsTo.posZ - anchor.posZ));

        // TODO need to flip these around, they are getting the location right but the opposite side then rotating backwards casing it to be flipped


        this.rotation.y = basicRotation(constraintVert.x, constraintVert.z);

        this.rotation.x = (float) (Math.PI * 0.5) + basicRotation(-constraintVert.y, (float) Math.sqrt(constraintVert.x * constraintVert.x + constraintVert.z * constraintVert.z));

        this.updatePosition();

        this.updatePosDifference();
    }

    protected void updatePosition() {
        this.position = new PointD(anchor.posX, anchor.posY, anchor.posZ);
    }


    /**
     * Convert to using Math.atan2(y,x);
     *
     * @param axis1
     * @param axis2
     * @return
     */
    public float basicRotation(float axis1, float axis2) {
        return (float) Math.atan2(axis1, axis2);
    }

}
