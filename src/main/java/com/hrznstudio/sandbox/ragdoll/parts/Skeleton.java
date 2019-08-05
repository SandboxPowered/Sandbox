package com.hrznstudio.sandbox.ragdoll.parts;

import com.hrznstudio.sandbox.client.entity.RagdollEntity;
import com.hrznstudio.sandbox.maths.PointD;
import com.hrznstudio.sandbox.maths.VectorMaths;
import com.hrznstudio.sandbox.ragdoll.parts.trackers.Tracker;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by sekwah on 8/4/2015.
 * <p>
 * // TODO add some detection to contstraints for when they are massively disshaped or something similar and try reversing it
 * sometimes, should fix legs crossing over glitch but could be made worse if the entity is squashed for whatever reason.
 * just add some tests to see if they work.
 */
public class Skeleton {

    //public SkeletonPoint[] points;

    public List<SkeletonPoint> points = new ArrayList<>();

    //public Constraint[] constraints;

    public List<Constraint> constraints = new ArrayList<>();

    //public Triangle[] triangles;

    public List<Triangle> triangles = new ArrayList<>();

    public int maxUpdateCount = 5;

    public int updateCount = 0;

    // Store a velocity which is the last position take away the current position but also add it so you can add velocity
    //  because if its added for a single update itll carry on that motion. So stuff like explosions or an arrow to the
    //  knee. Also if a player walks around a ragdoll you can add sorta a magnetic push for parts near entities away from the player.
    //  but do that after all physics works :3 (also for collisions with blocks, use the moving points like arrows do and stuff
    //  try to use current mc stuff to figure out where it can and cant go.

    public Skeleton() {

    }

    public boolean isActive() {
        for (SkeletonPoint point : this.points) {
            if (point.hasMoved) {
                return true;
            }
        }
        return false;
    }

    /**
     * Applies the physics before constraints are taken into account.
     *
     * @param entity
     */
    public void update(RagdollEntity entity) {

        this.storeTemp(entity);

        for (SkeletonPoint point : points) {
            point.update(entity);
        }

        int updates = 0;
        while (updates++ <= this.maxUpdateCount) {
            if (!this.isActive()) {
                break;
            }
            for (Constraint constraint : constraints) {
                constraint.calc(entity);
            }
        }

        this.updateCount = updates;

        for (SkeletonPoint point : points) {
            point.updatePos(entity);
        }

        for (Tracker tracker : entity.ragdoll.trackerHashmap.values()) {
            tracker.calcPosition();
        }

        this.loadTemp(entity);

    }

    private void storeTemp(RagdollEntity entity) {
        entity.noClip = false;
        entity.tempPosX = entity.x;
        entity.tempPosY = entity.y;
        entity.tempPosZ = entity.z;
        entity.tempBoundingBox = entity.getBoundingBox();
    }

    private void loadTemp(RagdollEntity entity) {
        entity.noClip = true;
        entity.x = entity.tempPosX;
        entity.y = entity.tempPosY;
        entity.z = entity.tempPosZ;
        entity.setBoundingBox(entity.tempBoundingBox);
    }

    public void renderSkeletonDebug() {
        renderSkeletonDebug(0);
    }

    /**
     * Renders all the constraints as lines, also maybe add the linked triangles next. Create a skleton for the
     *
     * @param activeStatus
     */
    public void renderSkeletonDebug(int activeStatus) {
        glDisable(GL_CULL_FACE);
        glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        for (Triangle triangle : triangles) {
            switch (activeStatus) {
                case 0:
                    glColor4f(0.0f, 0.8f, 0.1f, 0.5f);
                    break;
                case 1:
                    glColor4f(0.8f, 0.6f, 0.0f, 0.5f);
                    break;
                case 2:
                    glColor4f(0.8f, 0.0f, 0.0f, 0.5f);
                    break;
            }
            drawTriangle(triangle.points[0], triangle.points[1], triangle.points[2]);
            glColor3f(1f, 1f, 1f);
        }
        glEnable(GL_CULL_FACE);
        for (Constraint constraint : constraints) {
            // getBrightness(float p_70013_1_) from entity
            switch (activeStatus) {
                case 0:
                    glColor4f(0.0f, 1.0f, 0.2f, 0.8f);
                    break;
                case 1:
                    glColor4f(1.0f, 0.7f, 0.0f, 0.5f);
                    break;
                case 2:
                    glColor4f(1.0f, 0.0f, 0.0f, 0.5f);
                    break;
            }
            drawLine(constraint.end[0], constraint.end[1]);
            glColor4f(1f, 1f, 1f, 1.0f);

        }
        for (Triangle triangle : triangles) {
            // getBrightness(float p_70013_1_) from entity
            glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
            PointD direction = triangle.getDirection();
            PointD normal = triangle.getNormal();
            PointD basePoint = triangle.points[0].toPoint();
            PointD directionPoint = basePoint.add(direction);
            PointD normalPoint = basePoint.add(normal.multiply(4));
            drawLine(basePoint, directionPoint);
            glColor4f(0f, 0f, 1f, 1.0f);
            drawLine(basePoint, normalPoint);
            glColor4f(1f, 1f, 1f, 1.0f);
        }
        glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    public void drawLine(SkeletonPoint point, SkeletonPoint point2) {
        glBegin(GL_LINE_STRIP);
        glVertex3d(point.posX, point.posY, point.posZ);
        glVertex3d(point2.posX, point2.posY, point2.posZ);
        glEnd();
    }

    public void drawLine(PointD point, PointD point2) {
        glBegin(GL_LINE_STRIP);
        glVertex3d(point.x, point.y, point.z);
        glVertex3d(point2.x, point2.y, point2.z);
        glEnd();
    }

    public void drawTriangle(SkeletonPoint point, SkeletonPoint point2, SkeletonPoint point3) {
        glBegin(GL_TRIANGLES);
        //glColor3f(0.1, 0.2, 0.3);
        glVertex3d(point.posX, point.posY, point.posZ);
        glVertex3d(point2.posX, point2.posY, point2.posZ);
        glVertex3d(point3.posX, point3.posY, point3.posZ);
        glEnd();
    }

    public void verifyPoints(RagdollEntity entity) {
        this.storeTemp(entity);
        for (SkeletonPoint point : points) {
            point.verify(entity);
        }
        for (Tracker tracker : entity.ragdoll.trackerHashmap.values()) {
            tracker.updateLastPos();
            tracker.updatePosDifference();
        }
        this.loadTemp(entity);
    }

    public void shiftPos(double x, double y, double z) {
        for (SkeletonPoint point : points) {
            point.shiftPosition(x, y, z);
            //point.movePoint(entity);
        }
    }

    public void setVelocity(double motionX, double motionY, double motionZ) {
        for (SkeletonPoint point : points) {
            point.setVelocity(motionX, motionY, motionZ);
            //point.movePoint(entity);
        }
    }

    public void addVelocity(double motionX, double motionY, double motionZ) {
        for (SkeletonPoint point : points) {
            point.addVelocity(motionX, motionY, motionZ);
            //point.movePoint(entity);
        }
    }

    public void rotate(float rotYaw) {
        for (SkeletonPoint point : this.points) {
            PointD newLoc = VectorMaths.rotateOriginY(Math.toRadians(-rotYaw), point.toPoint());
            point.setPosition(newLoc);
        }
    }
}
