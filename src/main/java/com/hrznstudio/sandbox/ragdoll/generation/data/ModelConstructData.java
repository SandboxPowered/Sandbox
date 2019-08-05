package com.hrznstudio.sandbox.ragdoll.generation.data;

import com.hrznstudio.sandbox.ragdoll.generation.data.tracker.TriangleTrackerData;
import com.hrznstudio.sandbox.ragdoll.generation.data.tracker.VertexTrackerData;

import java.util.ArrayList;

/**
 * Store data about the entity
 * <p>
 * Created by sekwah41 on 29/07/2017.
 */
public class ModelConstructData {

    /**
     * Can only contain string int boolean float double and long for now.
     * <p>
     * More can be added as they need to have support added.
     * <p>
     * Add them into the RagdollGenerator
     */
    private Object[] constructData = new Object[0];

    private ArrayList<VertexTrackerData> vertexTrackers = new ArrayList<>();

    private ArrayList<TriangleTrackerData> triangleTrackers = new ArrayList<>();

    private String className;

    public String getClassName() {
        return className;
    }

    /**
     * Ignores null values
     *
     * @param className
     */
    public void setClassName(String className) {
        if (className == null) {
            return;
        }
        this.constructData = new Object[0];
        this.className = className;
    }

    public Object[] getConstructData() {
        return constructData;
    }

    public void setConstructData(Object[] constructData) {
        this.constructData = constructData;
    }

    public void addVertexTracker(VertexTrackerData vertexTrackerData) {
        this.vertexTrackers.add(vertexTrackerData);
    }

    public void addTriangleTracker(TriangleTrackerData trackerData) {
        this.triangleTrackers.add(trackerData);
    }

    public VertexTrackerData[] getVertexTrackerData() {
        return this.vertexTrackers.toArray(new VertexTrackerData[0]);
    }

    public TriangleTrackerData[] getTriangleTrackerData() {
        return this.triangleTrackers.toArray(new TriangleTrackerData[0]);
    }
}
