package com.hrznstudio.sandbox.client;

import com.hrznstudio.sandbox.api.IDownloadIndicator;
import com.hrznstudio.sandbox.util.FileUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class DownloadScreen extends Screen {
    public static Color DARK = new Color(0x951213);
    public static Color RED = new Color(0xD23131);
    public static Color WHITE = new Color(0xFFFFFFF);
    private final String[] dls = new String[]{
            "https://ttt.playhrzn.com/sbox/5MB.zip",
            "https://ttt.playhrzn.com/sbox/10MB.zip",
            "https://ttt.playhrzn.com/sbox/100MB.zip",
    };
    private int addon = 0;
    private IDownloadIndicator dl;

    public DownloadScreen() {
        super(new LiteralText(""));
        nextAddon();
    }

    public void nextAddon() {
        if (addon + 1 != dls.length) {
            try {
                dl = FileUtil.downloadFile(new URL(dls[addon]), Paths.get("server/ttt.playhrzn.com/cache/addon" + addon + ".sbx"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            addon++;
        }
    }

    public static String humanReadableByteCount(long bytes) {
        return humanReadableByteCount(bytes, 2);
    }

    public static String humanReadableByteCount(long bytes, int depth) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        char pre = ("KMGTPE").charAt(exp - 1);
        return String.format("%." + depth + "f %sB", bytes / Math.pow(unit, exp), pre);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        if (dl.isComplete()) {
            nextAddon();
        }

        String right = "Preparing Addon " + addon + " of " + dls.length;
        String right2 = "";
        if (dl.hasStarted()) {
            right = "Downloading Addon " + addon + " of " + dls.length;
            int percent = (int) ((dl.getCurrentSize() * 100) / dl.getTotalSize());
            right2 = percent + "% " + humanReadableByteCount(dl.getCurrentSize()) + "/" + humanReadableByteCount(dl.getTotalSize());
        }
        fill(0, 0, width, height, RED.getRGB());
        fill(0, height - 20, width, height, DARK.darker().getRGB());
        fill(width - font.getStringWidth(right) - 4, height - 34, width, height, DARK.darker().getRGB());
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlphaTest();
        minecraft.getTextureManager().bindTexture(new Identifier("sandbox", "textures/gui/sandbox.png"));
        GlStateManager.color4f(1, 1, 1, 1);
        int int_6 = (this.minecraft.window.getScaledWidth() - 256) / 2;
        int int_8 = (this.minecraft.window.getScaledHeight() - 256) / 2;
        this.blit(int_6, int_8, 0, 0, 256, 256);
        GlStateManager.popMatrix();
        drawCenteredString(font, "Connecting to Sandbox", (int) (width / 2f), (int) ((height / 2f) + (width / 3) / 2) - 20, WHITE.getRGB());
        drawRightText(right, width, height - 30, WHITE.getRGB());
        drawCenteredString(font, right2, width - (font.getStringWidth(right) / 2), height - 14, WHITE.getRGB());
        font.drawWithShadow("Joining Private Session", 3, height - 14, WHITE.getRGB());
    }

    public void drawRightText(String text, int x, int y, int color) {
        font.drawWithShadow(text, x - font.getStringWidth(text) - 1, y, color);
    }
}