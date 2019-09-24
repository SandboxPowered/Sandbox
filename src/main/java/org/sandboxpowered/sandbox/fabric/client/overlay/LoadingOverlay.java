package org.sandboxpowered.sandbox.fabric.client.overlay;

import org.sandboxpowered.sandbox.api.IDownloadIndicator;
import org.sandboxpowered.sandbox.fabric.client.SandboxClient;
import org.sandboxpowered.sandbox.fabric.util.FileUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class LoadingOverlay extends Overlay {
    public static Color DARK = new Color(0x951213);
    public static Color RED = new Color(0xD23131);
    public static Color WHITE = new Color(0xFFFFFFF);
    private final MinecraftClient client;
    private final String prefix;
    private final List<Pair<String, String>> addons;
    private int addon = 0;
    private IDownloadIndicator dl;

    public LoadingOverlay(MinecraftClient client, String prefix, List<Pair<String, String>> addons) {
        this.client = client;
        this.prefix = prefix;
        this.addons = addons;
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
        if (addon < addons.size()) {
            try {
                Pair<String, String> urlHash = addons.get(addon);
                Path path = Paths.get("server/cache/" + urlHash.getRight());
                if (Files.notExists(path))
                    dl = FileUtil.downloadFile(new URL(prefix + urlHash.getLeft()), path);
                else {
                    addon++;
                    nextAddon();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            addon++;
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        int width = this.client.window.getScaledWidth();
        int height = this.client.window.getScaledHeight();
        String right = "Preparing Addon " + addon + " of " + addons.size();
        String right2 = "";
        if (dl == null) {
            nextAddon();
        }
        if (dl != null) {
            if (dl.hasStarted()) {
                right = "Downloading Addon " + addon + " of " + addons.size();
                int percent = (int) ((dl.getCurrentSize() * 100) / dl.getTotalSize());
                right2 = percent + "% " + humanReadableByteCount(dl.getCurrentSize()) + "/" + humanReadableByteCount(dl.getTotalSize());
            }
            if (dl.isComplete()) {
                right = "Completed Addon Download " + addon + " of " + addons.size();
                nextAddon();
            }
        }
        if (addon >= addons.size()) {
            client.setOverlay(null);
            SandboxClient.INSTANCE.load(addons.stream().map(addon -> Paths.get("server/cache/" + addon.getRight())).collect(Collectors.toList()));
        }
        fill(0, 0, width, height, RED.getRGB());
        fill(0, height - 20, width, height, DARK.darker().getRGB());
        fill(width - client.textRenderer.getStringWidth(right) - 4, height - 34, width, height, DARK.darker().getRGB());
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.enableAlphaTest();
        client.getTextureManager().bindTexture(new Identifier("sandbox", "textures/gui/sandbox.png"));
        GlStateManager.color4f(1, 1, 1, 1);
        int int_6 = (this.client.window.getScaledWidth() - 256) / 2;
        int int_8 = (this.client.window.getScaledHeight() - 256) / 2;
        this.blit(int_6, int_8, 0, 0, 256, 256);
        GlStateManager.popMatrix();
        drawCenteredString(client.textRenderer, "Connecting to Sandbox", (int) (width / 2f), (int) ((height / 2f) + (width / 3) / 2) - 20, WHITE.getRGB());
        drawRightText(right, width, height - 30, WHITE.getRGB());
        drawCenteredString(client.textRenderer, right2, width - (client.textRenderer.getStringWidth(right) / 2), height - 14, WHITE.getRGB());
        client.textRenderer.drawWithShadow("Joining Private Session", 3, height - 14, WHITE.getRGB());
    }

    public void drawRightText(String text, int x, int y, int color) {
        client.textRenderer.drawWithShadow(text, x - client.textRenderer.getStringWidth(text) - 1, y, color);
    }
}