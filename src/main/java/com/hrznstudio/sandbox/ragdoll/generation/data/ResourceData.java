package com.hrznstudio.sandbox.ragdoll.generation.data;

/**
 * Created by sekwah41 on 05/08/2017.
 */
public class ResourceData {

    private final String textureDomain;
    private final String texture;

    public ResourceData(String textureDomain, String texture) {

        this.textureDomain = textureDomain;
        this.texture = texture;
    }

    public String getTextureDomain() {
        return textureDomain;
    }

    public String getTexture() {
        return texture;
    }
}
