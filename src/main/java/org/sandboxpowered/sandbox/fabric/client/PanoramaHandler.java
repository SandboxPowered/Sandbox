package org.sandboxpowered.sandbox.fabric.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotUtils;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class PanoramaHandler {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    public static File panoramaDir;
    public static File currentDir;
    public static float rotationYaw, rotationPitch;
    public static int panoramaStep;
    public static boolean takingPanorama;
    public static int currentWidth, currentHeight;
    public static int panoramaSize = 1024;
    public static boolean fullscreen = false;

    public static void takeScreenshot(Consumer<Text> consumer) {
        if (takingPanorama)
            return;

        takingPanorama = true;
        panoramaStep = 0;

        if (panoramaDir == null)
            panoramaDir = new File("screenshots", "panoramas");
        if (!panoramaDir.exists() && !panoramaDir.mkdirs()) return;

        String ts = getTimestamp();
        do {
            if (fullscreen) {
                currentDir = new File(panoramaDir + "_fullres", ts);
            } else {
                currentDir = new File(panoramaDir, ts);
            }
        } while (currentDir.exists());

        if (!currentDir.mkdirs()) return;

        Text panoramaDirComponent = new LiteralText(currentDir.getName());
        panoramaDirComponent.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, currentDir.getAbsolutePath())).withFormatting(Formatting.UNDERLINE);
        consumer.accept(new LiteralText("Panorama saved as ").append(panoramaDirComponent));
    }

    public static void renderTick(boolean start) {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.player != null && takingPanorama) {
            if (start) {
                if (panoramaStep == 0) {
                    mc.options.hudHidden = true;
                    currentWidth = mc.getWindow().getWidth();
                    currentHeight = mc.getWindow().getHeight();
                    rotationYaw = mc.player.yaw;
                    rotationPitch = mc.player.pitch;

                    if (!fullscreen)
                        GLFW.glfwSetWindowSize(MinecraftClient.getInstance().getWindow().getHandle(), panoramaSize, panoramaSize);
                }

                switch (panoramaStep) {
                    case 1:
                        mc.player.yaw = 180;
                        mc.player.pitch = 0;
                        break;
                    case 2:
                        mc.player.yaw = -90;
                        mc.player.pitch = 0;
                        break;
                    case 3:
                        mc.player.yaw = 0;
                        mc.player.pitch = 0;
                        break;
                    case 4:
                        mc.player.yaw = 90;
                        mc.player.pitch = 0;
                        break;
                    case 5:
                        mc.player.yaw = 180;
                        mc.player.pitch = -90;
                        break;
                    case 6:
                        mc.player.yaw = 180;
                        mc.player.pitch = 90;
                        break;
                }
                mc.player.prevYaw = mc.player.yaw;
                mc.player.prevPitch = mc.player.pitch;
            } else {
                if (panoramaStep > 0)
                    saveScreenshot(currentDir, "panorama_" + (panoramaStep - 1) + ".png", mc.getWindow().getWidth(), mc.getWindow().getHeight(), mc.getFramebuffer());
                panoramaStep++;
                if (panoramaStep == 7) {
                    mc.options.hudHidden = false;
                    takingPanorama = false;

                    mc.player.yaw = rotationYaw;
                    mc.player.pitch = rotationPitch;
                    mc.player.prevYaw = mc.player.yaw;
                    mc.player.prevPitch = mc.player.pitch;

                    GLFW.glfwSetWindowSize(MinecraftClient.getInstance().getWindow().getHandle(), currentWidth, currentHeight);
                }
            }
        }
    }

    private static void saveScreenshot(File dir, String screenshotName, int width, int height, Framebuffer buffer) {
        try {
            NativeImage bufferedImage = ScreenshotUtils.takeScreenshot(width, height, buffer);
            File file2 = new File(dir, screenshotName);

            bufferedImage.writeFile(file2);
        } catch (Exception exception) {
        }
    }

    private static String getTimestamp() {
        return DATE_FORMAT.format(new Date());
    }
}
