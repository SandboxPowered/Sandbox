package org.sandboxpowered.sandbox.fabric.util.wrapper;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import org.sandboxpowered.sandbox.api.client.render.RenderUtil;
import org.sandboxpowered.sandbox.api.util.Identity;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

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
    public void drawRepeating(int x, int y, int u, int v, int width, int height, float repeatWidth, float repeatHeight, int texWidth, int texHeight) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator_1 = Tessellator.getInstance();
        BufferBuilder bufferBuilder_1 = tessellator_1.getBufferBuilder();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        bufferBuilder_1.begin(7, VertexFormats.POSITION_UV_COLOR);
        bufferBuilder_1.vertex(x, y + height, 0.0D).texture(u / (float) texWidth, (v + repeatHeight) / (float) texHeight).color(255, 255, 255, 255).next();
        bufferBuilder_1.vertex(x + width, y + height, 0.0D).texture((u + repeatWidth) / (float) texWidth, (float) (v + repeatHeight) / (float) texHeight).color(255, 255, 255, 255).next();
        bufferBuilder_1.vertex(x + width, y, 0.0D).texture((u + repeatWidth) / (float) texWidth, v / (float) texHeight).color(255, 255, 255, 255).next();
        bufferBuilder_1.vertex(x, y, 0.0D).texture(u / (float) texWidth, v / (float) texHeight).color(255, 255, 255, 255).next();
        tessellator_1.draw();
    }
}