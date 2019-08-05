package com.hrznstudio.sandbox.ragdoll.ragdolls;

import com.hrznstudio.sandbox.client.entity.RagdollEntity;
import com.hrznstudio.sandbox.ragdoll.parts.Skeleton;
import com.hrznstudio.sandbox.ragdoll.parts.SkeletonPoint;
import com.hrznstudio.sandbox.ragdoll.parts.Triangle;
import com.hrznstudio.sandbox.ragdoll.parts.trackers.*;
import net.minecraft.client.model.Cuboid;
import net.minecraft.client.model.Model;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sekawh on 8/4/2015.
 */
public class BaseRagdoll {

    public final Model baseModel;

    public Map<Cuboid, Tracker> trackerHashmap = new HashMap<>();

    public boolean trackersRegistered = false;

    // Current skeleton position and shape
    public Skeleton skeleton;

    // offset from the bottom of the desired entity to the main point of the ragdoll
    public double centerHeightOffset;

    public Identifier resourceLocation;

    public BaseRagdoll(float centerHeightOffset, Model baseModel) {

        this.baseModel = baseModel;

        this.skeleton = new Skeleton();

        this.centerHeightOffset = centerHeightOffset;
    }

    public void rotateRagdoll(float rotYaw) {
        this.skeleton.rotate(rotYaw);
    }

    /**
     * Called whenever an update is needed
     *
     * @param entity
     */
    public void update(RagdollEntity entity) {
        skeleton.update(entity);
    }

    public void shiftPos(double x, double y, double z) {
        skeleton.shiftPos(x, y, z);
    }

    public void setStanceToEntity(LivingEntity entity) {
        for (SkeletonPoint point : skeleton.points) {
            // Finish rotation maths
            //newPoint.translate(new Vector3f((float) point.posX, (float) point.posY, (float) point.posZ));
            //SekCPhysics.logger.info(entity.rotationYaw);
            Vec3d vec = new Vec3d(point.posX, point.posY, point.posZ);
            vec.rotateY((float) Math.toRadians(-entity.yaw));
            point.setPosition(vec.x, vec.y, vec.z);
        }
    }

    public void initTrackers(Model model) {
        trackersRegistered = true;
    }

    protected void addVertexTracker(Cuboid part, SkeletonPoint anchor, SkeletonPoint pointTo, float scale) {
        if (scale == 1) {
            trackerHashmap.put(part, new TrackerVertex(part, anchor, pointTo));
        } else {
            trackerHashmap.put(part, new TrackerVertexScaled(part, anchor, pointTo, scale));
        }
    }

    protected void addTriangleTracker(Cuboid part, Triangle triangle, float scale) {
        if (scale == 1) {
            trackerHashmap.put(part, new TrackerTriangle(part, triangle));
        } else {
            trackerHashmap.put(part, new TrackerTriangleScaled(part, triangle, scale));
        }
    }

    protected void addTriangleTracker(Cuboid part, Triangle triangle, float rotateOffsetX, float rotateOffsetY, float rotateOffsetZ, float scale) {
        if (scale == 1) {
            trackerHashmap.put(part, new TrackerTriangle(part, triangle, rotateOffsetX, rotateOffsetY, rotateOffsetZ));
        } else {
            trackerHashmap.put(part, new TrackerTriangleScaled(part, triangle, rotateOffsetX, rotateOffsetY, rotateOffsetZ, scale));
        }
    }

    public boolean isActive() {
        return this.skeleton.isActive();
    }

    public int activeStatus() {
        //System.out.println(this.skeleton.updateCount);
        if (this.isActive()) {
            return 0;
        } else {
            return this.skeleton.updateCount == 1 ? 2 : 1;
        }
    }
}
