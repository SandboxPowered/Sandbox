package com.hrznstudio.sandbox.ragdoll.parts;

import com.hrznstudio.sandbox.client.entity.RagdollEntity;
import com.hrznstudio.sandbox.maths.PointD;
import com.hrznstudio.sandbox.ragdoll.Ragdolls;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

/**
 * Created by sekawh on 8/5/2015.
 */
public class SkeletonPoint {

    private final float size;

    public double posX;
    public double posY;
    public double posZ;

    public double lastPosX;
    public double lastPosY;
    public double lastPosZ;

    public double newPosX;
    public double newPosY;
    public double newPosZ;

    private double nonMoveThresh = 0.001;

    public boolean hasMoved = true;

    // Push force multiplier
    public float pushability = 1;


    // basically distance from last point, just sounds nicer for when other forces interact with it or if you wanna set it
    //  from an explosion
    public double velX = 0;
    public double velY = 0;
    public double velZ = 0;

    private boolean onGround = false;

    private boolean inWater;

    /**
     * this will set positions to scale in terms of the model, also y coordinates on models are negative so reverse it manually
     */
    public SkeletonPoint(double x, double y, double z, float size) {
        this(x,y,z,size,true);
    }

    public SkeletonPoint(double x, double y, double z, boolean shouldDoModelScale) {
        this(x,y,z,0.15f,shouldDoModelScale);
    }

    public SkeletonPoint(double x, double y, double z) {
        this(x,y,z,0.15f,true);
    }

    // note the position is in blocks not the model locations, and every 1 block is split into 16 for the model positions(i think)
    public SkeletonPoint(double x, double y, double z, float size, boolean shouldDoModelScale) {
        this.setPosition(x,y,z);

        // Added to stop ragdolls becoming lines or acting in only 1 plane after hitting a wall
        float sizeRandom = (float) Math.random();
        float maxOffset = 0.001f;
        size += -maxOffset + maxOffset * 2f * sizeRandom;

        this.size = size;

        if(shouldDoModelScale) {
            shiftPositionToModelScale();
        }
    }

    public void shiftPositionToModelScale() {
        this.setPosition(this.posX / 16f, this.posY / 16f, this.posZ / 16f);
    }

    public void shiftPositionToWorldScale() {
        this.setPosition(this.posX * 16f, this.posY * 16f, this.posZ * 16f);
    }

    public void setPosition(double x, double y, double z) {
        this.lastPosX = this.newPosX = this.posX = x;
        this.lastPosY = this.newPosY = this.posY = y;
        this.lastPosZ = this.newPosZ = this.posZ = z;
        this.checkWillMove();
    }

    private void movePoint(RagdollEntity entity, Vec3d moveVec) {

        double pointPosX = entity.tempPosX + this.posX;
        double pointPosY = entity.tempPosY + this.posY;
        double pointPosZ = entity.tempPosZ + this.posZ;

        entity.setBoundingBox(new Box(pointPosX - size, pointPosY - size, pointPosZ - size,
                pointPosX + size, pointPosY + size, pointPosZ + size));

        entity.move(MovementType.SELF, moveVec);

        Box boundingBox = entity.getBoundingBox();

        this.posX = (boundingBox.minX + boundingBox.maxX) / 2.0D - entity.tempPosX;
        this.posY = (boundingBox.minY + boundingBox.maxY) / 2.0D - entity.tempPosY;
        this.posZ = (boundingBox.minZ + boundingBox.maxZ) / 2.0D - entity.tempPosZ;

        // Second half is to counteract slightly odd onGround values despite always being on ground (check to see if gravity is being applied properly)
        this.onGround = entity.onGround || Math.abs(this.lastPosY - this.posY) < 0.01f;

        //applyMove(vec3d_2);
        this.checkWillMove();
    }

    private void applyMove(Vec3d vec3d) {
        this.posX += vec3d.x;
        this.posY += vec3d.y;
        this.posZ += vec3d.z;
    }

    /**
     * Check if it will move and store a variable saying if it has. and set a min move amount
     * @return
     */
    private boolean checkWillMove() {
        this.nonMoveThresh = 0.0001f;

        double move = Math.abs(this.newPosX - this.posX) + Math.abs(this.newPosY - this.posY) + Math.abs(this.newPosZ - this.posZ);
        boolean moved = move < this.nonMoveThresh;
        //System.out.println(move);
        //System.out.println(moved);
        if(!moved) {
            this.newPosX = this.posX;
            this.newPosY = this.posY;
            this.newPosZ = this.posZ;
        }
        return this.hasMoved = moved;
    }

    private boolean isAboveSpeedThreashold(double vel) {
        return Math.abs(vel) > nonMoveThresh;
    }

    public void update(RagdollEntity entity) {
        this.velX = this.posX - this.lastPosX;
        if(!isAboveSpeedThreashold(this.velX)) {
            this.velX = 0;
        }
        this.velY = this.posY - this.lastPosY;
        if(!isAboveSpeedThreashold(this.velX)) {
            this.velX = 0;
        }
        this.velZ = this.posZ - this.lastPosZ;
        if(!isAboveSpeedThreashold(this.velX)) {
            this.velX = 0;
        }
        float speedMulti = 0.9999f;

        this.velY *= speedMulti;

        if(onGround) {
            float groundMulti = 0.85f;
            this.velX *= groundMulti;
            this.velZ *= groundMulti;
        }
        else{
            this.velX *= speedMulti;
            this.velZ *= speedMulti;
        }


        this.onGround = false;

        this.lastPosX = this.posX;
        this.lastPosY = this.posY;
        this.lastPosZ = this.posZ;

        Box axisalignedbb = this.getBoundingBox(entity);

        this.velY -= Ragdolls.gravity;

        // TODO check how the player gets this, i has roughly been coded for 1.13 as a test
        if (entity.world.containsBlockWithMaterial(axisalignedbb.expand(0.0D, -0.4000000059604645D, 0.0D).shrink(0.001D, 0.001D, 0.001D), Material.WATER)) {
            this.addVelocity(0, 0.06f, 0);
            if(!this.inWater) {
                this.inWater = true;
                this.velX *= 0.9f;
                this.velY *= 0.5f;
                this.velZ *= 0.9f;
            }
            else {
                this.velX *= 0.9f;
                this.velY *= 0.85f;
                this.velZ *= 0.9f;
            }
            //entity.setVelocity(0,0,0);
        }
        else {
            this.inWater = false;
        }

        this.updateCollisions(entity);

        this.movePoint(entity, new Vec3d(this.velX, this.velY, this.velZ));

        // TODO collisions work but something not updating right

        //next_old_position = position             // This position is the next frame'mixins old_position
        // position += position - old_position;     // Verlet integration
        //position += gravity;                     // gravity == (0,-0.01,0)

        if(this.checkWillMove()) {
            this.newPosX = this.posX;
            this.newPosY = this.posY;
            this.newPosZ = this.posZ;
        }
    }

    private Box getBoundingBox(RagdollEntity entity) {
        double pointPosX = entity.x + this.posX;
        double pointPosY = entity.y + this.posY;
        double pointPosZ = entity.z + this.posZ;

        return new Box(pointPosX - size, pointPosY - size, pointPosZ - size,
                pointPosX + size, pointPosY + size, pointPosZ + size);
    }

    // Wont push other entities but make it get pushed by others.
    private void updateCollisions(RagdollEntity entity) {
        double pointPosX = entity.x + this.posX;
        double pointPosY = entity.y + this.posY;
        double pointPosZ = entity.z + this.posZ;

        Box boundingBox = new Box(pointPosX - size, pointPosY - size, pointPosZ - size,
                pointPosX + size, pointPosY + size, pointPosZ + size);

        List list = entity.world.getEntities(entity, boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && !list.isEmpty())
        {
            for (int i = 0; i < list.size(); ++i)
            {
                Entity entityCol = (Entity)list.get(i);

                if(entityCol.isPushable()) {
                    this.collideWithEntity(entity, entityCol);
                }
            }
        }

        this.checkWillMove();

    }

    private void collideWithEntity(RagdollEntity entity, Entity entityCol) {
        double pointPosX = entity.x + this.posX;
        double pointPosZ = entity.z + this.posZ;
        double d0 = pointPosX - entityCol.x;
        double d1 = pointPosZ - entityCol.z;
        double d2 = MathHelper.absMax(d0, d1);

        if (d2 >= 0.009999999776482582D)
        {
            d2 = (double) MathHelper.sqrt(d2);
            d0 /= d2;
            d1 /= d2;
            double d3 = 1.0D / d2;

            if (d3 > 1.0D)
            {
                d3 = 1.0D;
            }

            d0 *= d3;
            d1 *= d3;
            d0 *= 0.05000000074505806D;
            d1 *= 0.05000000074505806D;
            // field_5968 should be pushSpeedModifier
            d0 *= (double)(1.0F - entityCol.pushSpeedReduction);
            d1 *= (double)(1.0F - entityCol.pushSpeedReduction);
            //SekCPhysics.logger.info(entityCol.motionX);
            //entityCol.addVelocity(-d0, 0.0D, -d1);
            this.addVelocity(d0 + entityCol.getVelocity().x, 0.0D, d1 + entityCol.getVelocity().z);
        }
    }

    public void setNewPos(double x, double y, double z) {
        this.newPosX = x;
        this.newPosY = y;
        this.newPosZ = z;
    }

    public void updatePos(RagdollEntity entity) {
        moveTo(entity, this.newPosX, this.newPosY, this.newPosZ);
    }

    // TODO check the movements and which values are global vs local
    public void moveTo(RagdollEntity entity, double x, double y, double z) {

        this.movePoint(entity, new Vec3d(x - this.posX, y - this.posY, z - this.posZ));

        /*this.posX = x;
        this.posY = y;
        this.posZ = z;*/

    }


    public PointD toPoint() {
        return new PointD(this.posX, this.posY, this.posZ);
    }

    public void verify(RagdollEntity entity) {
        double tempPosX = this.posX;
        double tempPosY = this.posY;
        double tempPosZ = this.posZ;

        this.posX = 0;
        this.posY = 0;
        this.posZ = 0;

        this.moveTo(entity, tempPosX, tempPosY, tempPosZ);

        this.lastPosX = this.posX;
        this.lastPosY = this.posY;
        this.lastPosZ = this.posZ;
    }

    public void shiftPosition(double x, double y, double z) {
        this.posX += x;
        this.posY += y;
        this.posZ += z;

        this.lastPosX += x;
        this.lastPosY += y;
        this.lastPosZ += z;
    }

    public void setVelocity(double motionX, double motionY, double motionZ) {
        this.lastPosX = this.posX - motionX;
        this.lastPosY = this.posY - motionY;
        this.lastPosZ = this.posZ - motionZ;
    }

    public void addVelocity(double motionX, double motionY, double motionZ) {
        this.lastPosX -= motionX;
        this.lastPosY -= motionY;
        this.lastPosZ -= motionZ;
    }


    public void setNewPos(PointD newLoc) {
        this.setNewPos(newLoc.x, newLoc.y, newLoc.z);
    }

    public void setPosition(PointD newLoc) {
        this.setPosition(newLoc.x, newLoc.y, newLoc.z);
    }
}
