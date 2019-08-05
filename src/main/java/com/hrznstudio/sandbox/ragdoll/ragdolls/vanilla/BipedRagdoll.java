package com.hrznstudio.sandbox.ragdoll.ragdolls.vanilla;

import com.hrznstudio.sandbox.ragdoll.parts.Constraint;
import com.hrznstudio.sandbox.ragdoll.parts.SkeletonPoint;
import com.hrznstudio.sandbox.ragdoll.parts.Triangle;
import com.hrznstudio.sandbox.ragdoll.ragdolls.BaseRagdoll;

/**
 * Created by sekawh on 8/5/2015.
 */
public class BipedRagdoll extends BaseRagdoll {

    private final Triangle headTriangle;

    private final Triangle bodyTriangle;

    public SkeletonPoint headLeft;
    public SkeletonPoint headRight;

    public SkeletonPoint leftLegBot;
    public SkeletonPoint rightLegBot;
    public SkeletonPoint centerTorso;
    public SkeletonPoint leftShoulder;
    public SkeletonPoint rightShoulder;
    public SkeletonPoint leftLegTop;
    public SkeletonPoint rightLegTop;
    public SkeletonPoint rightArm;
    public SkeletonPoint leftArm;

    public BipedRagdoll() {
        super(24, null);

        centerTorso = new SkeletonPoint(0f, 0f, 0f);

        headLeft = new SkeletonPoint(3f, 7f, 0f);

        headRight = new SkeletonPoint(-3f, 7f, 0f);

        leftShoulder = new SkeletonPoint(5f, -2f, 0f);

        rightShoulder = new SkeletonPoint(-5f, -2f, 0f);

        leftLegTop = new SkeletonPoint(2f, -12f, 0f);

        rightLegTop = new SkeletonPoint(-2f, -12f, 0f);

        /*rightArm = new SkeletonPoint(-6f,-11f,0f);

        leftArm = new SkeletonPoint(6f,-11f,0f);*/

        rightArm = new SkeletonPoint(-14f, -2f, 1f);

        leftArm = new SkeletonPoint(14f, -2f, 1f);

        leftLegBot = new SkeletonPoint(2f, -23f, 0f);

        rightLegBot = new SkeletonPoint(-2f, -23f, 0f);

        skeleton.points.add(centerTorso);
        skeleton.points.add(leftShoulder);
        skeleton.points.add(rightShoulder);
        skeleton.points.add(leftLegTop);
        skeleton.points.add(rightLegTop);

        skeleton.points.add(leftArm);
        skeleton.points.add(rightArm);

        skeleton.points.add(leftLegBot);
        skeleton.points.add(rightLegBot);

        skeleton.points.add(headLeft);
        skeleton.points.add(headRight);

        skeleton.constraints.add(new Constraint(centerTorso, headLeft));
        skeleton.constraints.add(new Constraint(centerTorso, headRight));
        skeleton.constraints.add(new Constraint(headLeft, headRight));

        skeleton.constraints.add(new Constraint(leftShoulder, leftArm));
        skeleton.constraints.add(new Constraint(rightShoulder, rightArm));

        skeleton.constraints.add(new Constraint(leftLegTop, leftLegBot));
        skeleton.constraints.add(new Constraint(rightLegTop, rightLegBot));


        skeleton.constraints.add(new Constraint(centerTorso, leftShoulder));
        skeleton.constraints.add(new Constraint(centerTorso, rightShoulder));

        skeleton.constraints.add(new Constraint(centerTorso, leftLegTop));
        skeleton.constraints.add(new Constraint(centerTorso, rightLegTop));
        skeleton.constraints.add(new Constraint(leftLegTop, rightLegTop));

        skeleton.constraints.add(new Constraint(leftLegTop, leftShoulder));
        skeleton.constraints.add(new Constraint(rightLegTop, rightShoulder));

        skeleton.constraints.add(new Constraint(leftShoulder, rightShoulder));

        skeleton.constraints.add(new Constraint(rightLegTop, leftShoulder));

        skeleton.constraints.add(new Constraint(leftLegTop, rightShoulder));

        headTriangle = new Triangle(centerTorso, headLeft, headRight);

        skeleton.triangles.add(headTriangle);

        bodyTriangle = new Triangle(centerTorso, leftLegTop, rightLegTop);

        skeleton.triangles.add(bodyTriangle);

        //skeleton.triangles.add(new Triangle(centerTorso, leftLegTop, leftShoulder));

        //skeleton.triangles.add(new Triangle(centerTorso, rightLegTop, rightShoulder));

        //trackers.add(new TrackerVertex())


        // write code to add a list to the array, it makes it easier.

    }

    /*public void initTrackers(class_3879 model) {
        super.initTrackers(model);
        if(model instanceof ModelBiped) {
            ModelBiped modelBiped = (ModelBiped) model;
            this.addVertexTracker(modelBiped.bipedRightArm, this.rightShoulder, this.rightArm, creationData.getScale());
            this.addVertexTracker(modelBiped.bipedLeftArm, this.leftShoulder, this.leftArm, creationData.getScale());

            this.addVertexTracker(modelBiped.bipedRightLeg, this.rightLegTop, this.rightLegBot, creationData.getScale());
            this.addVertexTracker(modelBiped.bipedLeftLeg, this.leftLegTop, this.leftLegBot, creationData.getScale());

            this.addTriangleTracker(modelBiped.bipedBody, this.bodyTriangle, 180f, 0, 0, creationData.getScale());
            this.addTriangleTracker(modelBiped.bipedHead, this.headTriangle, 0, 0, 0, creationData.getScale());
            //this.addTriangleTracker(modelBiped.bipedHead, this.headTriangle);
        }
        else{
            SekCPhysics.logger.error("Model type invalid!");
        }
    }*/

}
