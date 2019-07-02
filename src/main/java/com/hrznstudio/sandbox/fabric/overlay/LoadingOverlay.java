package com.hrznstudio.sandbox.fabric.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashScreen;
import net.minecraft.resource.ResourceReloadMonitor;
import net.minecraft.util.Identifier;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.math.MathHelper;

import java.awt.*;

//TODO: redesign the screen
public class LoadingOverlay extends SplashScreen {
    private static final Identifier LOGO = new Identifier("sandbox:textures/gui/sandbox.png");
    private final MinecraftClient client;
    private final ResourceReloadMonitor reloadMonitor;
    private final Runnable field_18218;
    private final boolean field_18219;
    private float field_17770;
    private long field_17771 = -1L;
    private long field_18220 = -1L;

    public LoadingOverlay(MinecraftClient minecraftClient_1, ResourceReloadMonitor resourceReloadMonitor_1, Runnable runnable_1, boolean boolean_1) {
        super(minecraftClient_1, resourceReloadMonitor_1, runnable_1, boolean_1);
        this.client=minecraftClient_1;
        this.reloadMonitor=resourceReloadMonitor_1;
        this.field_18218=runnable_1;
        this.field_18219=boolean_1;
    }

    @Override
    public void render(int int_1, int int_2, float float_1) {
        int int_3 = this.client.window.getScaledWidth();
        int int_4 = this.client.window.getScaledHeight();
        long long_1 = SystemUtil.getMeasuringTimeMs();
        if (this.field_18219 && (this.reloadMonitor.isLoadStageComplete() || this.client.currentScreen != null) && this.field_18220 == -1L) {
            this.field_18220 = long_1;
        }

        float float_2 = this.field_17771 > -1L ? (float)(long_1 - this.field_17771) / 1000.0F : -1.0F;
        float float_3 = this.field_18220 > -1L ? (float)(long_1 - this.field_18220) / 500.0F : -1.0F;
        float float_6;
        int int_6;
        if (float_2 >= 1.0F) {
            if (this.client.currentScreen != null) {
                this.client.currentScreen.render(0, 0, float_1);
            }

            int_6 = MathHelper.ceil((1.0F - MathHelper.clamp(float_2 - 1.0F, 0.0F, 1.0F)) * 255.0F);
            fill(0, 0, int_3, int_4, 13775153 | int_6 << 24);
            float_6 = 1.0F - MathHelper.clamp(float_2 - 1.0F, 0.0F, 1.0F);
        } else if (this.field_18219) {
            if (this.client.currentScreen != null && float_3 < 1.0F) {
                this.client.currentScreen.render(int_1, int_2, float_1);
            }

            int_6 = MathHelper.ceil(MathHelper.clamp((double)float_3, 0.15D, 1.0D) * 255.0D);
            fill(0, 0, int_3, int_4, 13775153 | int_6 << 24);
            float_6 = MathHelper.clamp(float_3, 0.0F, 1.0F);
        } else {
            fill(0, 0, int_3, int_4, new Color(13775153).getRGB());
            float_6 = 1.0F;
        }

        int_6 = (this.client.window.getScaledWidth() - 256) / 2;
        int int_8 = (this.client.window.getScaledHeight() - 256) / 2;
        this.client.getTextureManager().bindTexture(LOGO);
        GlStateManager.enableBlend();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, float_6);
        this.blit(int_6, int_8, 0, 0, 256, 256);
        float float_7 = this.reloadMonitor.getProgress();
        this.field_17770 = this.field_17770 * 0.95F + float_7 * 0.050000012F;
        if (float_2 < 1.0F) {
            this.renderProgressBar(int_3 / 2 - 150, int_4 / 4 * 3, int_3 / 2 + 150, int_4 / 4 * 3 + 10, this.field_17770, 1.0F - MathHelper.clamp(float_2, 0.0F, 1.0F));
        }

        if (float_2 >= 2.0F) {
            this.client.setOverlay((Overlay)null);
        }

        if (this.field_17771 == -1L && this.reloadMonitor.isApplyStageComplete() && (!this.field_18219 || float_3 >= 2.0F)) {
            this.reloadMonitor.throwExceptions();
            this.field_17771 = SystemUtil.getMeasuringTimeMs();
            this.field_18218.run();
            if (this.client.currentScreen != null) {
                this.client.currentScreen.init(this.client, this.client.window.getScaledWidth(), this.client.window.getScaledHeight());
            }
        }

    }
    private void renderProgressBar(int int_1, int int_2, int int_3, int int_4, float float_1, float float_2) {
        int int_5 = MathHelper.ceil((float)(int_3 - int_1 - 2) * float_1);
        fill(int_1 - 1, int_2 - 1, int_3 + 1, int_4 + 1, -16777216 | Math.round((1.0F - float_2) * 255.0F) << 16 | Math.round((1.0F - float_2) * 255.0F) << 8 | Math.round((1.0F - float_2) * 255.0F));
        fill(int_1, int_2, int_3, int_4, -1);
        fill(int_1 + 1, int_2 + 1, int_1 + int_5, int_4 - 1, -16777216 | (int)MathHelper.lerp(1.0F - float_2, 226.0F, 255.0F) << 16 | (int)MathHelper.lerp(1.0F - float_2, 40.0F, 255.0F) << 8 | (int)MathHelper.lerp(1.0F - float_2, 55.0F, 255.0F));
    }
}
