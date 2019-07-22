package com.hrznstudio.sandbox.ragdoll.generation.data.tracker;

import com.google.gson.JsonObject;

/**
 * Created by sekwah41 on 30/07/2017.
 */
public class VertexTrackerData extends TrackerData {

    public final String anchor;
    public final String pointTo;

    public VertexTrackerData(String partName, String anchor, String pointTo, JsonObject vertexObj) {
        super(partName, vertexObj);
        this.anchor = anchor;
        this.pointTo = pointTo;
    }

}
