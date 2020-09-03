package org.sandboxpowered.sandbox.fabric.service.rendering;

import org.sandboxpowered.api.client.rendering.ui.Sprite;
import org.sandboxpowered.api.util.Identity;

public class FabricUISprite implements Sprite {
    private final Identity id;
    private final Identity texture;
    private final int posX, posY, sizeX, sizeY;
    private final int top, bottom, left, right;

    public FabricUISprite(Identity id, Identity texture, int posX, int posY, int sizeX, int sizeY, int top, int bottom, int left, int right) {
        this.id = id;
        this.texture = texture;
        this.posX = posX;
        this.posY = posY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }

    @Override
    public Identity getTexture() {
        return texture;
    }

    @Override
    public Identity getId() {
        return id;
    }

    @Override
    public int getPositionX() {
        return posX;
    }

    @Override
    public int getPositionY() {
        return posY;
    }

    @Override
    public int getSizeX() {
        return sizeX;
    }

    @Override
    public int getSizeY() {
        return sizeY;
    }

    @Override
    public int getBorderTop() {
        return top;
    }

    @Override
    public int getBorderBottom() {
        return bottom;
    }

    @Override
    public int getBorderLeft() {
        return left;
    }

    @Override
    public int getBorderRight() {
        return right;
    }
}