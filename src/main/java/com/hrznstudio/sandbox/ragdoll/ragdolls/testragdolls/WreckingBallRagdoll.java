package com.hrznstudio.sandbox.ragdoll.ragdolls.testragdolls;

import com.hrznstudio.sandbox.ragdoll.parts.AnchoredSkeletonPoint;
import com.hrznstudio.sandbox.ragdoll.parts.Constraint;
import com.hrznstudio.sandbox.ragdoll.parts.SkeletonPoint;
import com.hrznstudio.sandbox.ragdoll.ragdolls.BaseRagdoll;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sekawh on 8/5/2015.
 */
public class WreckingBallRagdoll extends BaseRagdoll {

    public WreckingBallRagdoll() {
        super(1.4f, null);

        centerHeightOffset = 24;

        AnchoredSkeletonPoint anchorPoint = new AnchoredSkeletonPoint(0, 0, 0);

        SkeletonPoint chain1 = new SkeletonPoint(1, 0, 0, false);

        SkeletonPoint chain2 = new SkeletonPoint(2, 0, 0, false);

        SkeletonPoint topbox = new SkeletonPoint(3, 0, 0, false);

        SkeletonPoint botbox = new SkeletonPoint(4, 0, 0, false);

        SkeletonPoint leftbox = new SkeletonPoint(3.5, -0.5, 0, false);

        SkeletonPoint rightbox = new SkeletonPoint(3.5, 0.5, 0, false);

        SkeletonPoint backbox = new SkeletonPoint(3.5, 0, 0.5, false);

        SkeletonPoint frontbox = new SkeletonPoint(3.5, 0, -0.5, false);

        skeleton.points.add(anchorPoint);

        skeleton.points.add(chain1);

        skeleton.points.add(chain2);

        skeleton.points.add(topbox);

        skeleton.points.add(botbox);

        skeleton.points.add(leftbox);

        skeleton.points.add(rightbox);

        skeleton.points.add(backbox);

        skeleton.points.add(frontbox);

        skeleton.constraints.add(new Constraint(anchorPoint, chain1));

        skeleton.constraints.add(new Constraint(chain1, chain2));

        skeleton.constraints.add(new Constraint(chain2, topbox));

        List<SkeletonPoint> pointsInBox = new ArrayList<SkeletonPoint>();

        pointsInBox.add(topbox);
        pointsInBox.add(botbox);
        pointsInBox.add(frontbox);
        pointsInBox.add(backbox);
        pointsInBox.add(leftbox);
        pointsInBox.add(rightbox);

        // Is rather rough and repeats constraints so takes longer but should be fine.
        for (SkeletonPoint point1 : pointsInBox) {
            for (SkeletonPoint point2 : pointsInBox) {
                if (point1 != point2) {
                    skeleton.constraints.add(new Constraint(point1, point2));
                }
            }
        }


        // write code to add a list to the array, it makes it easier.

    }

}
