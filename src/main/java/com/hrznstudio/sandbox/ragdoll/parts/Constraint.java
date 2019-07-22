package com.hrznstudio.sandbox.ragdoll.parts;

import com.hrznstudio.sandbox.client.entity.RagdollEntity;
import com.hrznstudio.sandbox.maths.PointD;

/**
 * Created by sekawh on 8/6/2015.
 *
 * Creates links between points for physics, if its fixed its like creating sticks between the points
 * and if the constraint is not then it can be any length in a range sorta like a unpowered piston or some sorta
 * sliding thing.
 */
public class Constraint {

    // so far will only be working with rigid constraints,
    //public boolean isRigid = true;

    // if using a non rigid then
    // public float[] length = new float[2];

    // but till they are needed

    // TODO add rotation constraints to single links which arn't triangles, would be good for simulating shoulders
    // but for now just add some stuff so that way its sorta rough physics(shoulders will have no leeway due to constraints
    //public boolean hasAngleConstraint = false;

    /**
     * 0 is x
     * 1 is y
     *
     * maybe make a seperate set of objects for angle constraints and stuff. Try to make it as efficient as possible.
     */
    //public float[] angleConstraint = new float[2];

    /**
     * Will be calculated when the constraints are created to stop any potential problems(although itll be fun to test
     * what happens if the number is massively out or something else happens.
     */
    public double length;

    /**
     * The points the constraint is attached to so at each end of the constraint.
     */
    public SkeletonPoint[] end = new SkeletonPoint[2];

    public Constraint(SkeletonPoint start, SkeletonPoint end) {
        this.end[0] = start;
        this.end[1] = end;
        this.length = Math.sqrt(Math.pow(start.posX - end.posX, 2) + Math.pow(start.posY - end.posY, 2) + Math.pow(start.posZ - end.posZ, 2));
    }

    public void calc(RagdollEntity entity) {

        if(!(end[0].hasMoved || end[1].hasMoved)) {
            //System.out.println("Cancel");
            return;
        }

        PointD averageLoc = new PointD((end[0].newPosX + end[1].newPosX) * 0.5d,
                (end[0].newPosY + end[1].newPosY) * 0.5d,(end[0].newPosZ + end[1].newPosZ) * 0.5d);

        double currentLength = Math.sqrt(Math.pow(end[0].newPosX - end[1].newPosX, 2) + Math.pow(end[0].newPosY - end[1].newPosY, 2) + Math.pow(end[0].newPosZ - end[1].newPosZ, 2));
        // If its already the correct length theres no point in recalculating
        if(currentLength == length) {
            return;
        }
        if(currentLength == 0) {
            currentLength = 0.01;
        }
        double currentLengthInvert = 1.0d/currentLength;

        PointD direction = new PointD((end[0].newPosX - end[1].newPosX) * currentLengthInvert,
                (end[0].newPosY - end[1].newPosY) * currentLengthInvert, (end[0].newPosZ - end[1].newPosZ) * currentLengthInvert);

        //System.out.println(averageLoc);

        double halfLength = length * 0.5d;

        //System.out.println("");

        //System.out.println(length);

        end[0].setNewPos(averageLoc.x + (direction.x * halfLength), averageLoc.y + (direction.y * halfLength),
                (float) (averageLoc.z + (direction.z * halfLength)));

        end[1].setNewPos(averageLoc.x - (direction.x * halfLength), averageLoc.y - (direction.y * halfLength),
                averageLoc.z - (direction.z * halfLength));
    }
}