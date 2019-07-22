package com.hrznstudio.sandbox.ragdoll.generation;

import com.hrznstudio.sandbox.maths.PointD;
import com.hrznstudio.sandbox.ragdoll.generation.data.ConstraintData;
import com.hrznstudio.sandbox.ragdoll.generation.data.RagdollData;
import com.hrznstudio.sandbox.ragdoll.generation.data.TriangleData;
import com.hrznstudio.sandbox.ragdoll.parts.Constraint;
import com.hrznstudio.sandbox.ragdoll.parts.SkeletonPoint;
import com.hrznstudio.sandbox.ragdoll.parts.Triangle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Build the data for the ragdoll from the RagdollData into new ragdoll data
 *
 * Only constructs the data that needs to be cloned though such as constraints. The rest of the data which is immutable
 * is from the RagdollData
 *
 * Created by sekwah41 on 06/07/2017.
 */
public class RagdollConstructor {

    private HashMap<String, SkeletonPoint> skeletonPointHashMap = new HashMap<>();

    private LinkedList<Constraint> constraintLinkedList = new LinkedList<>();

    private HashMap<String, Triangle> triangleHashMap = new HashMap<>();

    public RagdollConstructor(RagdollData creationData) {

        for(Map.Entry<String, PointD> entry : creationData.getPointMap().entrySet()) {
            PointD point = entry.getValue();
            this.skeletonPointHashMap.put(entry.getKey(), new SkeletonPoint(point.x, point.y, point.z,
                    0.15f * creationData.getScale()));
        }

        for(ConstraintData constraintData : creationData.getConstraints()) {
            this.constraintLinkedList.add(new Constraint(this.getSkeletonPoint(constraintData.part1),
                    this.getSkeletonPoint(constraintData.part2)));
        }

        for(Map.Entry<String, TriangleData> entry : creationData.getTriangleMap().entrySet()) {
            TriangleData triangleData = entry.getValue();
            this.triangleHashMap.put(entry.getKey(), new Triangle(this.getSkeletonPoint(triangleData.getPoint(0)),
                    this.getSkeletonPoint(triangleData.getPoint(1)), this.getSkeletonPoint(triangleData.getPoint(2))));
        }

    }

    /**
     * Data should never be null at this point, the null parts were from before while making the data for this
     * in RagdollData
     * @param point
     * @return
     */
    public SkeletonPoint getSkeletonPoint(String point) {
        SkeletonPoint skeletonPoint = this.skeletonPointHashMap.get(point);
        return skeletonPoint;
    }

    public SkeletonPoint[] getSkeletonPoints() {
        return this.skeletonPointHashMap.values().toArray(new SkeletonPoint[0]);
    }

    public Constraint[] getConstraints() {
        return this.constraintLinkedList.toArray(new Constraint[0]);
    }

    /**
     * Data should never be null at this point, the null parts were from before while making the data for this
     * in RagdollData
     * @param triangleName
     * @return
     */
    public Triangle getTriangle(String triangleName) {
        Triangle triangle = this.triangleHashMap.get(triangleName);
        return triangle;
    }

    public Triangle[] getTriangles() {
        return this.triangleHashMap.values().toArray(new Triangle[0]);
    }
}
