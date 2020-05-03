package org.sandboxpowered.sandbox.fabric.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.sandboxpowered.sandbox.fabric.internal.IDownloadIndicator;
import org.sandboxpowered.sandbox.fabric.util.FileUtil;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class DownloadScreen extends Screen {
    public static Color DARK = new Color(0x951213);
    public static Color RED = new Color(0xD23131);
    public static Color WHITE = new Color(0xFFFFFFF);
    private final String[] dls = new String[]{
            "http://ipv4.download.thinkbroadband.com/5MB.zip",
            "http://ipv4.download.thinkbroadband.com/10MB.zip"
    };
    private int addon = 0;
    private IDownloadIndicator dl;

    public DownloadScreen() {
        super(new LiteralText(""));
        nextAddon();
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

    public void nextAddon() {
        if (addon != dls.length) {
            try {
                dl = FileUtil.downloadFile(new URL(dls[addon]), Paths.get("server/cache/addon" + addon + ".sbx"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            addon++;
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        super.render(matrices, mouseX, mouseY, partialTicks);
        Text right = null;
        String right2 = "";
        if (dl.hasStarted()) {
            right = new LiteralText("Downloading Addon " + addon + " of " + dls.length);
            int percent = (int) ((dl.getCurrentSize() * 100) / dl.getTotalSize());
            right2 = percent + "% " + humanReadableByteCount(dl.getCurrentSize()) + "/" + humanReadableByteCount(dl.getTotalSize());
        }
        if (dl.isComplete()) {
            right = new LiteralText("Completed Addon Download " + addon + " of " + dls.length);
            nextAddon();
        }
        if (right == null) {
            right = new LiteralText("Preparing Addon " + addon + " of " + dls.length);
        }
        fill(matrices, 0, 0, width, height, RED.getRGB());
        fill(matrices, 0, height - 20, width, height, DARK.darker().getRGB());
        fill(matrices, width - textRenderer.getStringWidth(right) - 4, height - 34, width, height, DARK.darker().getRGB());
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlphaTest();
        client.getTextureManager().bindTexture(new Identifier("sandbox", "textures/gui/sandbox.png"));
        GlStateManager.color4f(1, 1, 1, 1);
        int int_6 = (this.client.getWindow().getScaledWidth() - 256) / 2;
        int int_8 = (this.client.getWindow().getScaledHeight() - 256) / 2;
        this.blit(int_6, int_8, 0, 0, 256, 256);
        GlStateManager.popMatrix();
        drawCenteredString(matrices, textRenderer, "Connecting to Sandbox", (int) (width / 2f), (int) ((height / 2f) + (width / 3) / 2) - 20, WHITE.getRGB());
        drawRightText(matrices, right, width, height - 30, WHITE.getRGB());
        drawCenteredString(matrices, textRenderer, right2, width - (textRenderer.getStringWidth(right) / 2), height - 14, WHITE.getRGB());
        textRenderer.drawWithShadow(matrices, "Joining Private Session", 3, height - 14, WHITE.getRGB());
    }

    public void drawRightText(MatrixStack matrices, Text text, int x, int y, int color) {
        textRenderer.drawWithShadow(matrices, text, x - textRenderer.getStringWidth(text) - 1, y, color);
    }
}