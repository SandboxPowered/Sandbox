package com.hrznstudio.sandbox.ragdoll.ragdolls.testragdolls;

import com.hrznstudio.sandbox.ragdoll.parts.AnchoredSkeletonPoint;
import com.hrznstudio.sandbox.ragdoll.parts.Constraint;
import com.hrznstudio.sandbox.ragdoll.parts.SkeletonPoint;
import com.hrznstudio.sandbox.ragdoll.ragdolls.BaseRagdoll;

/**
 * Created by sekawh on 8/5/2015.
 */
public class CurtainRagdoll extends BaseRagdoll {

    private int width = 4;

    private int height = 7;
    SkeletonPoint[][] leftPoints = new SkeletonPoint[width][height];
    SkeletonPoint[][] rightPoints = new SkeletonPoint[width][height];
    private float spacing = 0.25f;

    public CurtainRagdoll() {
        super(1.4f, null);

        centerHeightOffset = 24;

        // Top row (anchor points)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (y == 0) {
                    leftPoints[x][y] = new AnchoredSkeletonPoint(x * spacing, -y * spacing, 0, false);
                } else {
                    leftPoints[x][y] = new SkeletonPoint(x * spacing, -y * spacing, 0, false);
                }
                skeleton.points.add(leftPoints[x][y]);
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x < width - 1) {
                    skeleton.constraints.add(new Constraint(leftPoints[x][y], leftPoints[x + 1][y]));
                }
                if (y < height - 1) {
                    skeleton.constraints.add(new Constraint(leftPoints[x][y], leftPoints[x][y + 1]));
                }
            }
        }

        // Top row (anchor points)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (y == 0) {
                    rightPoints[x][y] = new AnchoredSkeletonPoint(-x * spacing, -y * spacing, 0, false);
                } else {
                    rightPoints[x][y] = new SkeletonPoint(-x * spacing, -y * spacing, 0, false);
                }
                skeleton.points.add(rightPoints[x][y]);
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x < width - 1) {
                    skeleton.constraints.add(new Constraint(rightPoints[x][y], rightPoints[x + 1][y]));
                }
                if (y < height - 1) {
                    skeleton.constraints.add(new Constraint(rightPoints[x][y], rightPoints[x][y + 1]));
                }
            }
        }
    }

}
