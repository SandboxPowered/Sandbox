package org.sandboxpowered.sandbox.fabric.service.rendering;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.sandboxpowered.api.client.rendering.ui.DynamicRenderer;
import org.sandboxpowered.api.client.rendering.ui.Sprite;
import org.sandboxpowered.api.shape.Box;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.util.math.MatrixStack;
import org.sandboxpowered.api.util.math.Vec3f;
import org.sandboxpowered.sandbox.fabric.util.JsonUtil;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class FabricDynamicRenderer implements DynamicRenderer {
    private static final Gson GSON = new GsonBuilder().create();
    private final Map<Identity, FabricUISprite> spriteMap = new HashMap<>();

    private static void drawSprite(MatrixStack stack, VertexConsumer consumer, Sprite sprite, int posX, int posY, int sizeX, int sizeY) {
        net.minecraft.client.util.math.MatrixStack matrixStack = WrappingUtil.convert(stack);
        Matrix4f model = matrixStack.peek().getModel();
        int width = posX + sizeX;
        int height = posY + sizeY;

        float uMin = sprite.getPositionX() / 256f;
        float uMax = (sprite.getPositionX() + sprite.getSizeX()) / 256f;
        float vMin = sprite.getPositionY() / 256f;
        float vMax = (sprite.getPositionY() + sprite.getSizeY()) / 256f;

        int borderTop = sprite.getBorderTop();
        int borderBottom = sprite.getBorderBottom();
        int borderLeft = sprite.getBorderLeft();
        int borderRight = sprite.getBorderRight();

        float borderTopDivide = sprite.getBorderTop() / 256f;
        float borderBottomDivide = sprite.getBorderBottom() / 256f;
        float borderLeftDivide = sprite.getBorderLeft() / 256f;
        float borderRightDivide = sprite.getBorderRight() / 256f;

        if (!sprite.hasBorder() || (sprite.getSizeX() == sizeX && sprite.getSizeY() == sizeY)) {
            drawSpriteSection(model, consumer, posX, posY, width, height, uMin, uMax, vMin, vMax);
        } else {
            if (borderLeft != 0) {
                if (borderTop != 0) {
                    drawSpriteSection(model, consumer, posX, posY, posX + borderLeft, posY + borderTop, uMin, uMin + borderLeftDivide, vMin, vMin + borderTopDivide);
                }
                if (borderBottom != 0) {
                    drawSpriteSection(model, consumer, posX, height - borderBottom, posX + borderLeft, height, uMin, uMin + borderLeftDivide, vMax - borderBottomDivide, vMax);
                }
                drawSpriteSection(model, consumer, posX, posY + borderTop, posX + borderLeft, height - borderBottom, uMin, uMin + borderLeftDivide, vMin + borderTopDivide, vMax - borderBottomDivide);
            }
            if (borderRight != 0) {
                if (borderTop != 0) {
                    drawSpriteSection(model, consumer, width - borderRight, posY, width, posY + borderTop, uMax - borderRightDivide, uMax, vMin, vMin + borderTopDivide);
                }
                if (borderBottom != 0) {
                    drawSpriteSection(model, consumer, width - borderRight, height - borderBottom, width, height, uMax - borderRightDivide, uMax, vMax - borderBottomDivide, vMax);
                }
                drawSpriteSection(model, consumer, width - borderRight, posY + borderTop, width, height - borderBottom, uMax - borderRightDivide, uMax, vMin + borderTopDivide, vMax - borderBottomDivide);
            }
            if (borderTop != 0) {
                drawSpriteSection(model, consumer, posX + borderLeft, posY, width - borderRight, posY + borderTop, uMin + borderLeftDivide, uMax - borderRightDivide, vMin, vMin + borderTopDivide);
            }
            if (borderBottom != 0) {
                drawSpriteSection(model, consumer, posX + borderLeft, height - borderBottom, width - borderRight, height, uMin + borderLeftDivide, uMax - borderRightDivide, vMax - borderBottomDivide, vMax);
            }
            drawSpriteSection(model, consumer, posX + borderLeft, posY + borderTop, width - borderRight, height - borderBottom, uMin + borderLeftDivide, uMax - borderRightDivide, vMin + borderTopDivide, vMax - borderBottomDivide);
        }
    }

    private static void drawSpriteSection(Matrix4f model, VertexConsumer consumer, int posX, int posY, int width, int height, float uMin, float uMax, float vMin, float vMax) {
        consumer.vertex(model, posX, height, 0).texture(uMin, vMax).color(255, 255, 255, 255).next();
        consumer.vertex(model, width, height, 0).texture(uMax, vMax).color(255, 255, 255, 255).next();
        consumer.vertex(model, width, posY, 0).texture(uMax, vMin).color(255, 255, 255, 255).next();
        consumer.vertex(model, posX, posY, 0).texture(uMin, vMin).color(255, 255, 255, 255).next();
    }

    public void empty() {
        spriteMap.clear();
    }

    public void loadSprite(ResourceManager resourceManager, Identifier atlasId) {
        Resource resource = null;
        try {
            resource = resourceManager.getResource(atlasId);
            JsonObject element = GSON.fromJson(new InputStreamReader(resource.getInputStream()), JsonObject.class);
            Identity textureId = Identity.of(atlasId.getNamespace(), atlasId.getPath().replace(".atlas.json", ".png"));
            element.entrySet().forEach(entry -> {
                String spriteId = entry.getKey();
                Identity id = Identity.of(atlasId.getNamespace(), spriteId);
                JsonObject sprite = entry.getValue().getAsJsonObject();
                JsonObject position = sprite.getAsJsonObject("position");
                JsonObject size = sprite.getAsJsonObject("size");
                int top = 0, bottom = 0, left = 0, right = 0;
                if (sprite.has("border")) {
                    JsonObject border = sprite.getAsJsonObject("border");
                    top = JsonUtil.getInt(border, "top");
                    bottom = JsonUtil.getInt(border, "bottom");
                    left = JsonUtil.getInt(border, "left");
                    right = JsonUtil.getInt(border, "right");
                }

                FabricUISprite previousSprite = spriteMap.put(id, new FabricUISprite(
                        id,
                        textureId,
                        JsonUtil.getInt(position, "x"), JsonUtil.getInt(position, "y"),
                        JsonUtil.getInt(size, "x"), JsonUtil.getInt(size, "y"),
                        top, bottom, left, right));
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (resource != null) {
                try {
                    resource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int spriteCount() {
        return spriteMap.size();
    }

    @Override
    public void renderBox(MatrixStack stack, int color, int posX, int posY, int sizeX, int sizeY) {
        DrawableHelper.fill(
                WrappingUtil.convert(stack),
                posX, posY, posX + sizeX, posY + sizeY, color
        );
    }

    @Override
    public Sprite getSprite(Identity identity) {
        return spriteMap.get(identity);
    }

    @Override
    public void renderSprite(MatrixStack stack, Sprite sprite, int posX, int posY, int sizeX, int sizeY) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        MinecraftClient.getInstance().getTextureManager().bindTexture(WrappingUtil.convert(sprite.getTexture()));
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);

        drawSprite(stack, bufferBuilder, sprite, posX, posY, sizeX, sizeY);

        bufferBuilder.end();

        BufferRenderer.draw(bufferBuilder);
    }

    @Override
    public void renderSpriteArray(MatrixStack stack, Sprite sprite, int posX, int posY, int sizeX, int sizeY, int columns, int rows) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        MinecraftClient.getInstance().getTextureManager().bindTexture(WrappingUtil.convert(sprite.getTexture()));
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);

        for (int c = 0; c < columns; c++) {
            for (int r = 0; r < rows; r++) {
                drawSprite(stack, bufferBuilder, sprite, posX + (sizeX * c), posY + (sizeY * r), sizeX, sizeY);
            }
        }

        bufferBuilder.end();

        BufferRenderer.draw(bufferBuilder);
    }

    @Override
    public void renderSliceSprite(MatrixStack stack, Sprite sprite, int posX, int posY, SliceDirection direction, float percentage) {
        if (sprite.hasBorder())
            throw new IllegalArgumentException("Unable to use sprite with border for sliced sprite");
        if (percentage == 0)
            return;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        MinecraftClient.getInstance().getTextureManager().bindTexture(WrappingUtil.convert(sprite.getTexture()));
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        net.minecraft.client.util.math.MatrixStack matrixStack = WrappingUtil.convert(stack);
        Matrix4f model = matrixStack.peek().getModel();

        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);

        float uMin = sprite.getPositionX() / 256f;
        float uMax = (sprite.getPositionX() + sprite.getSizeX()) / 256f;
        float vMin = sprite.getPositionY() / 256f;
        float vMax = (sprite.getPositionY() + sprite.getSizeY()) / 256f;

        switch (direction) {
            case UP:
                vMin = (sprite.getPositionY() + (sprite.getSizeY() * (1 - percentage))) / 256f;
                drawSpriteSection(model, bufferBuilder, posX, (int) (posY + (sprite.getSizeY() * (1 - percentage))), posX + sprite.getSizeX(), posY + sprite.getSizeY(), uMin, uMax, vMin, vMax);
                break;
            case DOWN:
                vMax = (sprite.getPositionY() + (sprite.getSizeY() * percentage)) / 256f;
                drawSpriteSection(model, bufferBuilder, posX, posY, posX + sprite.getSizeX(), (int) (posY + (sprite.getSizeY() * percentage)), uMin, uMax, vMin, vMax);
                break;
            case RIGHT:
                uMax = (sprite.getPositionX() + (sprite.getSizeX() * percentage)) / 256f;
                drawSpriteSection(model, bufferBuilder, posX, posY, (int) (posX + (sprite.getSizeX() * percentage)), posY + sprite.getSizeY(), uMin, uMax, vMin, vMax);
                break;
            case LEFT:
                uMin = (sprite.getPositionX() + (sprite.getSizeX() * (1 - percentage))) / 256f;
                drawSpriteSection(model, bufferBuilder, (int) (posX + (sprite.getSizeX() * (1 - percentage))), posY, posX + sprite.getSizeX(), posY + sprite.getSizeY(), uMin, uMax, vMin, vMax);
                break;
        }

        bufferBuilder.end();

        BufferRenderer.draw(bufferBuilder);
    }

    @Override
    public void drawBox(MatrixStack stack, org.sandboxpowered.api.client.rendering.VertexConsumer consumer, Box box, float r, float g, float b) {

    }

    @Override
    public void drawLine(MatrixStack stack, org.sandboxpowered.api.client.rendering.VertexConsumer consumer, Vec3f from, Vec3f to, float r, float g, float b) {

    }
}