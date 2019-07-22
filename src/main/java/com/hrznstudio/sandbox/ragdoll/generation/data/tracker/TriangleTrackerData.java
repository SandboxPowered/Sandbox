package com.hrznstudio.sandbox.ragdoll.generation.data.tracker;

import com.google.gson.JsonObject;

/**
 * Created by sekwah41 on 30/07/2017.
 */
public class TriangleTrackerData extends TrackerData {

    public final String tracker;

    public TriangleTrackerData(String partName, String tracker, JsonObject vertexObj) {
        super(partName, vertexObj);
        this.tracker = tracker;
    }

}
