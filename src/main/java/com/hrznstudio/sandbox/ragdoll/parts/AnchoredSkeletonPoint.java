package com.hrznstudio.sandbox.ragdoll.parts;

import com.hrznstudio.sandbox.client.entity.RagdollEntity;

/**
 * Created by sekawh on 8/5/2015.
 */
public class AnchoredSkeletonPoint extends SkeletonPoint {

    public double posX;
    public double posY;
    public double posZ;


    // basically distance from last point, just sounds nicer for when other forces interact with it or if you wanna set it
    //  from an explosion
    public double velX = 0;
    public double velY = 0;
    public double velZ = 0;

    private boolean onGround = false;

    /**
     * this will set positions to scale in terms of the model, also y coordinates on models are negative so reverse it manually
     */
    public AnchoredSkeletonPoint(double x, double y, double z, float size) {
        this(x, y, z, size, true);
    }

    public AnchoredSkeletonPoint(double x, double y, double z, boolean shouldDoModelScale) {
        this(x, y, z, 0.15f, shouldDoModelScale);
    }

    public AnchoredSkeletonPoint(double x, double y, double z) {
        this(x, y, z, 0.15f, true);
    }

    // note the position is in blocks not the model locations, and every 1 block is split into 16 for the model positions(i think)
    public AnchoredSkeletonPoint(double x, double y, double z, float size, boolean shouldDoModelScale) {
        super(x, y, z, size, shouldDoModelScale);
    }

    public void movePoint(RagdollEntity entity, double moveX, double moveY, double moveZ) {

    }

    public void verify(RagdollEntity entity) {

    }

    public void setNewPos(double x, double y, double z) {

    }

    public void moveTo(RagdollEntity entity, double x, double y, double z) {

    }

    public void update(RagdollEntity entity) {
        this.velX = 0;
        this.velY = 0;
        this.velZ = 0;
        // gravity == (0,-0.01,0)
    }
}
