package com.hrznstudio.sandbox.ragdoll.generation.data;

import com.hrznstudio.sandbox.maths.PointD;
import com.hrznstudio.sandbox.ragdoll.parts.Triangle;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Stores Ragdoll data for creation
 * <p>
 * Created by sekwah41 on 28/06/2017.
 */
public class RagdollData implements Cloneable {

    private HashMap<String, PointD> pointHashMap = new HashMap<>();

    private HashMap<String, TriangleData> triangleHashMap = new HashMap<>();

    /**
     * Has no references back so best to do it this way for quick use.
     */
    private LinkedList<ConstraintData> constraintLinkedList = new LinkedList<ConstraintData>();

    private HashMap<String, Triangle> trangleHashMap = new HashMap<>();

    private float centerHeightOffset;

    private ModelData modelData;

    private float scale = 1;

    public RagdollData() {
    }

    public void setSkeletonPoint(String pointName, double x, double y, double z) {
        this.pointHashMap.put(pointName, new PointD(x, y, -z));
    }

    /*public void setTriangle(String triangleName, String point1, String point2, String point3) throws RagdollInvalidDataException {
        this.trangleHashMap.put(triangleName, new Triangle(this.getPoint(point1),
                this.getPoint(point2), this.getPoint(point3)));
    }*/

    public void addConstraint(String point1, String point2) throws RagdollInvalidDataException {
        this.constraintLinkedList.add(new ConstraintData(this.checkPoint(point1), this.checkPoint(point2)));
    }

    public PointD getPoint(String point) throws RagdollInvalidDataException {
        PointD skeletonPoint = this.pointHashMap.get(point);
        if (skeletonPoint == null) {
            throw new RagdollInvalidDataException("Invalid Skeleton Point Selected");
        }
        return skeletonPoint;
    }

    public String checkPoint(String point) throws RagdollInvalidDataException {
        if (!this.pointHashMap.containsKey(point)) {
            throw new RagdollInvalidDataException("Invalid Skeleton Point Selected");
        }
        return point;
    }

    public PointD[] getPoints() {
        return this.pointHashMap.values().toArray(new PointD[0]);
    }

    public ConstraintData[] getConstraints() {
        return this.constraintLinkedList.toArray(new ConstraintData[0]);
    }

    public HashMap<String, PointD> getPointMap() {
        return this.pointHashMap;
    }

    public void addTriangle(String name, String point1, String point2, String point3) throws RagdollInvalidDataException {
        if (this.triangleHashMap.containsKey(name)) {
            throw new RagdollInvalidDataException("Already triangle with that name");
        }
        this.triangleHashMap.put(name, new TriangleData(point1, point2, point3));
    }

    public TriangleData[] getTriangles() {
        return this.triangleHashMap.values().toArray(new TriangleData[0]);
    }

    public HashMap<String, TriangleData> getTriangleMap() {
        return this.triangleHashMap;
    }

    public void addModelData(ModelData modelData) {
        this.modelData = modelData;
    }

    public ModelData getModelData() {
        return modelData;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getCenterHeightOffset() {
        return centerHeightOffset;
    }

    public void setCenterHeightOffset(float centerHeightOffset) {
        this.centerHeightOffset = centerHeightOffset;
    }

    @Override
    public RagdollData clone() {
        try {
            return (RagdollData) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
