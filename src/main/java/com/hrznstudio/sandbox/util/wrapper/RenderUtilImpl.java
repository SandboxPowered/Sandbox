package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.api.client.render.RenderUtil;
import com.hrznstudio.sandbox.api.util.Identity;
import com.hrznstudio.sandbox.util.WrappingUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;

public class RenderUtilImpl implements RenderUtil {
    public static final RenderUtil INSTANCE = new RenderUtilImpl();

    @Override
    public void draw(int x, int y, float u, float v, int width, int height, int texWidth, int texHeight) {
        DrawableHelper.blit(x, y, u, v, width, height, texWidth, texHeight);
    }

    @Override
    public void bind(Identity texture) {
        MinecraftClient.getInstance().getTextureManager().bindTexture(WrappingUtil.convert(texture));
    }

    @Override
    public void drawRepeating(int x, int y, int u, int v, int width, int height, float repeatWidth, float repeatHeight) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator_1 = Tessellator.getInstance();
        BufferBuilder bufferBuilder_1 = tessellator_1.getBufferBuilder();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        bufferBuilder_1.begin(7, VertexFormats.POSITION_UV_COLOR);
        bufferBuilder_1.vertex(x, y + height, 0.0D).texture(u/256f, (v + repeatHeight)/256f).color(255, 255, 255, 255).next();
        bufferBuilder_1.vertex(x + width, y + height, 0.0D).texture((u + repeatWidth)/256f, (float) (v + repeatHeight)/256f).color(255, 255, 255, 255).next();
        bufferBuilder_1.vertex(x + width, y, 0.0D).texture((u + repeatWidth)/256f, v/256f).color(255, 255, 255, 255).next();
        bufferBuilder_1.vertex(x, y, 0.0D).texture(u/256f, v/256f).color(255, 255, 255, 255).next();
        tessellator_1.draw();
    }
}