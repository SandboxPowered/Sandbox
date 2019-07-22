package com.hrznstudio.sandbox.ragdoll.generation.data.tracker;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.model.Cuboid;

/**
 * Created by sekwah41 on 06/07/2017.
 */
public class TrackerData {

    private final String partName;

    protected float posOffsetX;
    protected float posOffsetY;
    protected float posOffsetZ;

    protected float rotOffsetX;
    protected float rotOffsetY;
    protected float rotOffsetZ;

    public final boolean hasRotateData;

    private Cuboid part;

    public TrackerData(String partName, JsonObject vertexObj) {

        this.partName = partName;

        if(vertexObj == null) {
            this.hasRotateData = false;
            return;
        }
        this.hasRotateData = true;
        this.setPosOffsetX(vertexObj.get("posOffX"));
        this.setPosOffsetY(vertexObj.get("posOffY"));
        this.setPosOffsetZ(vertexObj.get("posOffZ"));

        this.setRotOffsetX(vertexObj.get("rotOffX"));
        this.setRotOffsetY(vertexObj.get("rotOffY"));
        this.setRotOffsetZ(vertexObj.get("rotOffZ"));

    }

    public void setRotOffsetX(JsonElement rotOffsetX) {
        if(rotOffsetX != null) {
            this.rotOffsetX = (float) Math.toRadians(rotOffsetX.getAsFloat());
        }
    }

    public float getRotOffsetX() {
        return rotOffsetX;
    }

    public float getRotOffsetY() {
        return rotOffsetY;
    }

    public void setRotOffsetY(JsonElement rotOffsetY) {
        if(rotOffsetY != null) {
            this.rotOffsetY = (float) Math.toRadians(rotOffsetY.getAsFloat());
        }
    }

    public float getRotOffsetZ() {
        return rotOffsetZ;
    }

    public void setRotOffsetZ(JsonElement rotOffsetZ) {
        if(rotOffsetZ != null) {
            this.rotOffsetZ = (float) Math.toRadians(rotOffsetZ.getAsFloat());
        }
    }

    public float getPosOffsetX() {
        return posOffsetX;
    }

    public void setPosOffsetX(JsonElement posOffsetX) {
        if(posOffsetX != null) {
            this.posOffsetX = posOffsetX.getAsFloat();
        }
    }

    public float getPosOffsetY() {
        return posOffsetY;
    }

    public void setPosOffsetY(JsonElement posOffsetY) {
        if(posOffsetY != null) {
            this.posOffsetY = posOffsetY.getAsFloat();
        }
    }

    public float getPosOffsetZ() {
        return posOffsetZ;
    }

    public void setPosOffsetZ(JsonElement posOffsetZ) {
        if(posOffsetZ != null) {
            this.posOffsetZ = posOffsetZ.getAsFloat();
        }
    }

    public Cuboid getPart() {
        return part;
    }

    public void setPart(Cuboid part) {
        this.part = part;
    }

    public String getPartName() {
        return partName;
    }
}
