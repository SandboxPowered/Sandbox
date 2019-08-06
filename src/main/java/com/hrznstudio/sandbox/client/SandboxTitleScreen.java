package com.hrznstudio.sandbox.client;

import com.google.common.util.concurrent.Runnables;
import com.hrznstudio.sandbox.Sandbox;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ChatUtil;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.LevelStorage;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SandboxTitleScreen extends Screen {
    public static final CubeMapRenderer PANORAMA_CUBE_MAP = new CubeMapRenderer(new Identifier("sandbox","textures/gui/panorama"));
    private static final Identifier PANORAMA_OVERLAY = new Identifier("textures/gui/title/background/panorama_overlay.png");
    private static final Identifier ACCESSIBILITY_ICON_TEXTURE = new Identifier("textures/gui/accessibility.png");
    private final boolean field_17776;
    @Nullable
    private String splashText;
    private ButtonWidget buttonResetDemo;
    @Nullable
    private SandboxTitleScreen.Warning warning;
    private static final Identifier MINECRAFT_TITLE_TEXTURE = new Identifier("textures/gui/title/minecraft.png");
    private static final Identifier EDITION_TITLE_TEXTURE = new Identifier("textures/gui/title/edition.png");
    private boolean realmsNotificationsInitialized;
    private Screen realmsNotificationGui;
    private int copyrightTextWidth;
    private int copyrightTextX;
    private final RotatingCubeMapRenderer backgroundRenderer;
    private final boolean doBackgroundFade;
    private long backgroundFadeStart;

    public SandboxTitleScreen() {
        this(false);
    }

    public SandboxTitleScreen(boolean boolean_1) {
        super(new TranslatableText("narrator.screen.title"));
        this.backgroundRenderer = new RotatingCubeMapRenderer(PANORAMA_CUBE_MAP);
        this.doBackgroundFade = boolean_1;
        this.field_17776 = (double) (new Random()).nextFloat() < 1.0E-4D;
        if (Sandbox.unsupportedModsLoaded) {
            this.warning = new Warning(
                    new LiteralText("Unsupported Mods Loaded").formatted(Formatting.RED).formatted(Formatting.BOLD),
                    new LiteralText("Installing other mods on top of sandbox is not supported").formatted(Formatting.RED),
                    "https://hrzn.atlassian.net/servicedesk/customer/portal/3"
            );
        }
        if (!GLX.supportsOpenGL2() && !GLX.isNextGen()) {
            this.warning = new SandboxTitleScreen.Warning((new TranslatableText("title.oldgl.eol.line1")).formatted(Formatting.RED).formatted(Formatting.BOLD), (new TranslatableText("title.oldgl.eol.line2")).formatted(Formatting.RED).formatted(Formatting.BOLD), "https://help.mojang.com/customer/portal/articles/325948?ref=game");
        }
    }

    private boolean areRealmsNotificationsEnabled() {
        return this.minecraft.options.realmsNotifications && this.realmsNotificationGui != null;
    }

    public void tick() {
        if (this.areRealmsNotificationsEnabled()) {
            this.realmsNotificationGui.tick();
        }

    }

    public static CompletableFuture<Void> loadTexturesAsync(TextureManager textureManager_1, Executor executor_1) {
        return CompletableFuture.allOf(textureManager_1.loadTextureAsync(MINECRAFT_TITLE_TEXTURE, executor_1), textureManager_1.loadTextureAsync(EDITION_TITLE_TEXTURE, executor_1), textureManager_1.loadTextureAsync(PANORAMA_OVERLAY, executor_1), PANORAMA_CUBE_MAP.loadTexturesAsync(textureManager_1, executor_1));
    }

    public boolean isPauseScreen() {
        return false;
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    protected void init() {
        if (this.splashText == null) {
            this.splashText = this.minecraft.getSplashTextLoader().get();
        }

        this.copyrightTextWidth = this.font.getStringWidth("Copyright Mojang AB. Do not distribute!");
        this.copyrightTextX = this.width - this.copyrightTextWidth - 2;
        int int_2 = this.height / 4 + 48;
        if (this.minecraft.isDemo()) {
            this.initWidgetsDemo(int_2, 24);
        } else {
            this.initWidgetsNormal(int_2, 24);
        }

        this.addButton(new TexturedButtonWidget(this.width / 2 - 124, int_2 + 72 + 12, 20, 20, 0, 106, 20, ButtonWidget.WIDGETS_LOCATION, 256, 256, (buttonWidget_1) -> {
            this.minecraft.openScreen(new LanguageOptionsScreen(this, this.minecraft.options, this.minecraft.getLanguageManager()));
        }, I18n.translate("narrator.button.language")));
        this.addButton(new ButtonWidget(this.width / 2 - 100, int_2 + 72 + 12, 98, 20, I18n.translate("menu.options"), (buttonWidget_1) -> {
            this.minecraft.openScreen(new SettingsScreen(this, this.minecraft.options));
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 2, int_2 + 72 + 12, 98, 20, I18n.translate("menu.quit"), (buttonWidget_1) -> {
            this.minecraft.scheduleStop();
        }));
        this.addButton(new TexturedButtonWidget(this.width / 2 + 104, int_2 + 72 + 12, 20, 20, 0, 0, 20, ACCESSIBILITY_ICON_TEXTURE, 32, 64, (buttonWidget_1) -> {
            this.minecraft.openScreen(new AccessibilityScreen(this, this.minecraft.options));
        }, I18n.translate("narrator.button.accessibility")));
        if (this.warning != null) {
            this.warning.init(int_2);
        }

        this.minecraft.setConnectedToRealms(false);
        if (this.minecraft.options.realmsNotifications && !this.realmsNotificationsInitialized) {
            RealmsBridge realmsBridge_1 = new RealmsBridge();
            this.realmsNotificationGui = realmsBridge_1.getNotificationScreen(this);
            this.realmsNotificationsInitialized = true;
        }

        if (this.areRealmsNotificationsEnabled()) {
            this.realmsNotificationGui.init(this.minecraft, this.width, this.height);
        }

    }

    private void initWidgetsNormal(int int_1, int int_2) {
        this.addButton(new ButtonWidget(this.width / 2 - 100, int_1, 200, 20, I18n.translate("menu.singleplayer"), (buttonWidget_1) -> {
            this.minecraft.openScreen(new SelectWorldScreen(this));
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 100, int_1 + int_2, 200, 20, I18n.translate("menu.multiplayer"), (buttonWidget_1) -> {
            this.minecraft.openScreen(new MultiplayerScreen(this));
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 100, int_1 + int_2 * 2, 200, 20, I18n.translate("menu.online"), (buttonWidget_1) -> {
            this.switchToRealms();
        }));
    }

    private void initWidgetsDemo(int int_1, int int_2) {
        this.addButton(new ButtonWidget(this.width / 2 - 100, int_1, 200, 20, I18n.translate("menu.playdemo"), (buttonWidget_1) -> {
            this.minecraft.startIntegratedServer("Demo_World", "Demo_World", MinecraftServer.DEMO_LEVEL_INFO);
        }));
        this.buttonResetDemo = this.addButton(new ButtonWidget(this.width / 2 - 100, int_1 + int_2, 200, 20, I18n.translate("menu.resetdemo"), (buttonWidget_1) -> {
            LevelStorage levelStorage_1 = this.minecraft.getLevelStorage();
            LevelProperties levelProperties_1 = levelStorage_1.getLevelProperties("Demo_World");
            if (levelProperties_1 != null) {
                this.minecraft.openScreen(new ConfirmScreen(this::method_20375, new TranslatableText("selectWorld.deleteQuestion"), new TranslatableText("selectWorld.deleteWarning", levelProperties_1.getLevelName()), I18n.translate("selectWorld.deleteButton"), I18n.translate("gui.cancel")));
            }

        }));
        LevelStorage levelStorage_1 = this.minecraft.getLevelStorage();
        LevelProperties levelProperties_1 = levelStorage_1.getLevelProperties("Demo_World");
        if (levelProperties_1 == null) {
            this.buttonResetDemo.active = false;
        }

    }

    private void switchToRealms() {
        RealmsBridge realmsBridge_1 = new RealmsBridge();
        realmsBridge_1.switchToRealms(this);
    }

    public void render(int int_1, int int_2, float float_1) {
        if (this.backgroundFadeStart == 0L && this.doBackgroundFade) {
            this.backgroundFadeStart = SystemUtil.getMeasuringTimeMs();
        }

        float float_2 = this.doBackgroundFade ? (float) (SystemUtil.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F : 1.0F;
        fill(0, 0, this.width, this.height, -1);
        this.backgroundRenderer.render(float_1, MathHelper.clamp(float_2, 0.0F, 1.0F));
        int int_4 = this.width / 2 - 137;
        this.minecraft.getTextureManager().bindTexture(PANORAMA_OVERLAY);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, this.doBackgroundFade ? (float) MathHelper.ceil(MathHelper.clamp(float_2, 0.0F, 1.0F)) : 1.0F);
        blit(0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
        float float_3 = this.doBackgroundFade ? MathHelper.clamp(float_2 - 1.0F, 0.0F, 1.0F) : 1.0F;
        int int_6 = MathHelper.ceil(float_3 * 255.0F) << 24;
        if ((int_6 & -67108864) != 0) {
            this.minecraft.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURE);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, float_3);
            if (this.field_17776) {
                this.blit(int_4 + 0, 30, 0, 0, 99, 44);
                this.blit(int_4 + 99, 30, 129, 0, 27, 44);
                this.blit(int_4 + 99 + 26, 30, 126, 0, 3, 44);
                this.blit(int_4 + 99 + 26 + 3, 30, 99, 0, 26, 44);
                this.blit(int_4 + 155, 30, 0, 45, 155, 44);
            } else {
                this.blit(int_4 + 0, 30, 0, 0, 155, 44);
                this.blit(int_4 + 155, 30, 0, 45, 155, 44);
            }

            this.minecraft.getTextureManager().bindTexture(EDITION_TITLE_TEXTURE);
            blit(int_4 + 88, 67, 0.0F, 0.0F, 98, 14, 128, 16);
            if (this.splashText != null) {
                GlStateManager.pushMatrix();
                GlStateManager.translatef((float) (this.width / 2 + 90), 70.0F, 0.0F);
                GlStateManager.rotatef(-20.0F, 0.0F, 0.0F, 1.0F);
                float float_4 = 1.8F - MathHelper.abs(MathHelper.sin((float) (SystemUtil.getMeasuringTimeMs() % 1000L) / 1000.0F * 6.2831855F) * 0.1F);
                float_4 = float_4 * 100.0F / (float) (this.font.getStringWidth(this.splashText) + 32);
                GlStateManager.scalef(float_4, float_4, float_4);
                this.drawCenteredString(this.font, this.splashText, 0, -8, 16776960 | int_6);
                GlStateManager.popMatrix();
            }

            String string_1 = "Minecraft " + SharedConstants.getGameVersion().getName();
            if (this.minecraft.isDemo()) {
                string_1 = string_1 + " Demo";
            } else {
                string_1 = string_1 + ("release".equalsIgnoreCase(this.minecraft.getVersionType()) ? "" : "/" + this.minecraft.getVersionType());
            }

            this.drawString(this.font, string_1, 2, this.height - 10, 16777215 | int_6);
            this.drawString(this.font, "Copyright Mojang AB. Do not distribute!", this.copyrightTextX, this.height - 10, 16777215 | int_6);
            if (int_1 > this.copyrightTextX && int_1 < this.copyrightTextX + this.copyrightTextWidth && int_2 > this.height - 10 && int_2 < this.height) {
                fill(this.copyrightTextX, this.height - 1, this.copyrightTextX + this.copyrightTextWidth, this.height, 16777215 | int_6);
            }

            if (this.warning != null) {
                this.warning.render(int_6);
            }

            Iterator var11 = this.buttons.iterator();

            while (var11.hasNext()) {
                AbstractButtonWidget abstractButtonWidget_1 = (AbstractButtonWidget) var11.next();
                abstractButtonWidget_1.setAlpha(float_3);
            }

            super.render(int_1, int_2, float_1);
            if (this.areRealmsNotificationsEnabled() && float_3 >= 1.0F) {
                this.realmsNotificationGui.render(int_1, int_2, float_1);
            }

        }
    }

    public boolean mouseClicked(double double_1, double double_2, int int_1) {
        if (super.mouseClicked(double_1, double_2, int_1)) {
            return true;
        } else if (this.warning != null && this.warning.onClick(double_1, double_2)) {
            return true;
        } else if (this.areRealmsNotificationsEnabled() && this.realmsNotificationGui.mouseClicked(double_1, double_2, int_1)) {
            return true;
        } else {
            if (double_1 > (double) this.copyrightTextX && double_1 < (double) (this.copyrightTextX + this.copyrightTextWidth) && double_2 > (double) (this.height - 10) && double_2 < (double) this.height) {
                this.minecraft.openScreen(new EndCreditsScreen(false, Runnables.doNothing()));
            }

            return false;
        }
    }

    public void removed() {
        if (this.realmsNotificationGui != null) {
            this.realmsNotificationGui.removed();
        }

    }

    private void method_20375(boolean boolean_1) {
        if (boolean_1) {
            LevelStorage levelStorage_1 = this.minecraft.getLevelStorage();
            levelStorage_1.deleteLevel("Demo_World");
        }

        this.minecraft.openScreen(this);
    }

    @Environment(EnvType.CLIENT)
    class Warning {
        private int line2Width;
        private int startX;
        private int startY;
        private int endX;
        private int endY;
        private final Text line1;
        private final Text line2;
        private final String helpUrl;

        public Warning(Text line1, Text line2, String url) {
            this.line1 = line1;
            this.line2 = line2;
            this.helpUrl = url;
        }

        public void init(int int_1) {
            int int_2 = SandboxTitleScreen.this.font.getStringWidth(this.line1.getString());
            this.line2Width = SandboxTitleScreen.this.font.getStringWidth(this.line2.getString());
            int int_3 = Math.max(int_2, this.line2Width);
            this.startX = (SandboxTitleScreen.this.width - int_3) / 2;
            this.startY = int_1 - 24;
            this.endX = this.startX + int_3;
            this.endY = this.startY + 24;
        }

        public void render(int int_1) {
            DrawableHelper.fill(this.startX - 2, this.startY - 2, this.endX + 2, this.endY - 1, 1428160512);
            SandboxTitleScreen.this.drawCenteredString(SandboxTitleScreen.this.font, this.line1.asFormattedString(), this.startX+((endX-startX)/2), this.startY, 16777215 | int_1);
            SandboxTitleScreen.this.drawString(SandboxTitleScreen.this.font, this.line2.asFormattedString(), (SandboxTitleScreen.this.width - this.line2Width) / 2, this.startY + 12, 16777215 | int_1);
        }

        public boolean onClick(double double_1, double double_2) {
            if (!ChatUtil.isEmpty(this.helpUrl) && double_1 >= (double) this.startX && double_1 <= (double) this.endX && double_2 >= (double) this.startY && double_2 <= (double) this.endY) {
                SandboxTitleScreen.this.minecraft.openScreen(new ConfirmChatLinkScreen((boolean_1) -> {
                    if (boolean_1) {
                        SystemUtil.getOperatingSystem().open(this.helpUrl);
                    }

                    SandboxTitleScreen.this.minecraft.openScreen(SandboxTitleScreen.this);
                }, this.helpUrl, true));
                return true;
            } else {
                return false;
            }
        }
    }
}