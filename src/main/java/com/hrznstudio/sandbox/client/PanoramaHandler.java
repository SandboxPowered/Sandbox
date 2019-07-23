package com.hrznstudio.sandbox.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlFramebuffer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotUtils;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class PanoramaHandler {

    public static File panoramaDir;
    public static File currentDir;
    public static float rotationYaw, rotationPitch;
    public static int panoramaStep;
    public static boolean takingPanorama;
    public static int currentWidth, currentHeight;
    public static boolean overridenOnce;
    public static int panoramaSize = 1024;
    public static boolean fullscreen = false;

    public static boolean takeScreenshot(Consumer<Text> consumer)  {
        if(takingPanorama)
            return false;

        takingPanorama = true;
        panoramaStep = 0;

        if(panoramaDir == null)
            panoramaDir = new File("screenshots", "panoramas");
        if(!panoramaDir.exists())
            panoramaDir.mkdirs();

        int i = 0;
        String ts = getTimestamp();
        do {
            if(fullscreen) {
                if(i == 0)
                    currentDir = new File(panoramaDir + "_fullres", ts);
                else currentDir = new File(panoramaDir, ts + "_" + i + "_fullres");
            } else {
                if(i == 0)
                    currentDir = new File(panoramaDir, ts);
                else currentDir = new File(panoramaDir, ts + "_" + i);
            }
        } while(currentDir.exists());

        currentDir.mkdirs();

        Text panoramaDirComponent = new LiteralText(currentDir.getName());
        panoramaDirComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, currentDir.getAbsolutePath())).setUnderline(true);
        consumer.accept(new LiteralText("Panorama saved as ").append(panoramaDirComponent));
        return true;
    }

    public static void renderTick(boolean start) {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (takingPanorama) {
            if (start) {
                if (panoramaStep == 0) {
                    mc.options.hudHidden = true;
                    currentWidth = mc.window.getWidth();
                    currentHeight = mc.window.getHeight();
                    rotationYaw = mc.player.yaw;
                    rotationPitch = mc.player.pitch;

                    if (!fullscreen)
                        GLFW.glfwSetWindowSize(MinecraftClient.getInstance().window.getHandle(), panoramaSize, panoramaSize);
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
                    saveScreenshot(currentDir, "panorama_" + (panoramaStep - 1) + ".png", mc.window.getWidth(), mc.window.getHeight(), mc.getFramebuffer());
                panoramaStep++;
                if (panoramaStep == 7) {
                    mc.options.hudHidden = false;
                    takingPanorama = false;

                    mc.player.yaw = rotationYaw;
                    mc.player.pitch = rotationPitch;
                    mc.player.prevYaw = mc.player.yaw;
                    mc.player.prevPitch = mc.player.pitch;

                    GLFW.glfwSetWindowSize(MinecraftClient.getInstance().window.getHandle(), currentWidth, currentHeight);
                }
            }
        }
    }


    private static void saveScreenshot(File dir, String screenshotName, int width, int height, GlFramebuffer buffer) {
        try {
            NativeImage bufferedimage = ScreenshotUtils.method_1663(width, height, buffer);
            File file2 = new File(dir, screenshotName);

            bufferedimage.writeFile(file2);
        } catch(Exception exception) { }
    }

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static String getTimestamp() {
        String s = DATE_FORMAT.format(new Date()).toString();
        return s;
    }
}
