package com.hrznstudio.sandbox.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.Random;

public class MultiplayerScreen extends Screen {
    public static Color DARK = new Color(0x951213);
    public static Color RED = new Color(0xD23131);
    public static Color WHITE = new Color(0xFFFFFFF);
    public static String[] str = new String[]{
    };
    private final CubeMapRenderer cubemap;

    private final RotatingCubeMapRenderer backgroundRenderer;

    public MultiplayerScreen() {
        super(new LiteralText(""));
        this.cubemap = new CubeMapRenderer(new Identifier("sandbox", "textures/gui/panorama/panorama"));
        this.backgroundRenderer = new RotatingCubeMapRenderer(cubemap);
    }

    @Override
    public void render(int int_1, int int_2, float float_1) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        fill(0, 0, this.width, this.height, -1);
        this.backgroundRenderer.render(float_1, MathHelper.clamp(1.0f, 0.0F, 1.0F));
        fill(250, 40, width - 40, height - 20, Integer.MIN_VALUE);
        renderListing(255, 45, width - 45 - 255, 45, "Survival", "29 Players on 4 server", "survival");
        renderListing(255, 45 + 50, width - 45 - 255, 45, "Creative", "22 Players on 3 server", "creative");
        renderListing(255, 45 + 100, width - 45 - 255, 45, "Trouble in Terrorist Town", "5 Players on 1 server", "ttt");
        renderListing(255, 45 + 150, width - 45 - 255, 45, "BlockHunt", "0 Players on 1 server", "gm_blockhunt");
        renderListing(255, 45 + 200, width - 45 - 255, 45, "Cinema", "0 Players on 1 server", "");

        fill(20, 40, 170, height - 20, Integer.MIN_VALUE);
        GlStateManager.pushMatrix();
        GlStateManager.translatef(25, 60, 0);
        GlStateManager.scalef(2, 2, 1);
        font.drawWithShadow("Server List", 0, 0, WHITE.getRGB());
        GlStateManager.popMatrix();
        font.drawWithShadow("Featured", 25, 120, WHITE.getRGB());
        font.drawWithShadow("Favourites", 25, 135, WHITE.getRGB());
        font.drawWithShadow("History", 25, 150, WHITE.getRGB());
        font.drawWithShadow("Direct Connect", 25, 165, WHITE.getRGB());
        font.drawWithShadow("Refresh", 25, 230, WHITE.getRGB());
        GlStateManager.popMatrix();

        super.render(int_1, int_2, float_1);
    }

    public void renderListing(int x, int y, int width, int height, String s, String s2, String s3) {
        fill(x, y, x + width, y + height, Integer.MIN_VALUE);
        if (StringUtils.isEmpty(s3)) {
            fill(x + width - 45, y, x + width, y + height, RED.getRGB());
        }
        minecraft.getTextureManager().bindTexture(new Identifier("sandbox", "textures/gui/" + (StringUtils.isEmpty(s3) ? "logo" : s3) + ".png"));
        GlStateManager.color4f(1, 1, 1, 1);
        innerBlit(x + 5, x + 40, y + 5, y + 40, 1, 0, 1, 0, 1);
        GlStateManager.pushMatrix();
        GlStateManager.translatef(x + 50, y + 5, 0);
        GlStateManager.scalef(2, 2, 1);
        font.drawWithShadow(s, 0, 0, WHITE.getRGB());
        GlStateManager.popMatrix();
        font.drawWithShadow(s2, x + 50, y + height - 5 - (font.fontHeight), WHITE.getRGB());
    }

    @Override
    public void renderBackground() {
        super.renderBackground();
    }
}