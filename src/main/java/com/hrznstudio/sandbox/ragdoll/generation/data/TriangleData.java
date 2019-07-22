package com.hrznstudio.sandbox.ragdoll.generation.data;

/**
 * Created by sekwah41 on 28/07/2017.
 */
public class TriangleData {

    private final String[] point = new String[3];

    public TriangleData(String point1, String point2, String point3) {
        this.point[0] = point1;
        this.point[1] = point2;
        this.point[2] = point3;
    }

    /**
     * Must be between 0 and 2 else it will throw a outofbounds error
     * @param pointNum
     * @return
     */
    public String getPoint(int pointNum) {
        return this.point[pointNum];
    }
}
