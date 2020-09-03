package org.sandboxpowered.sandbox.fabric.client.overlay;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.util.math.MatrixStack;
import org.sandboxpowered.sandbox.fabric.loader.SandboxLoader;

import java.awt.*;

public class V2LoadingOverlay extends Overlay {
    public static final Color DARK = new Color(0x951213);
    public static final Color RED = new Color(0xD23131);
    public static final Color WHITE = new Color(0xFFFFFFF);

    private final MinecraftClient client;
    private final SandboxLoader loader;

    public V2LoadingOverlay(MinecraftClient client, SandboxLoader loader) {
        this.client = client;
        this.loader = loader;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    }

    public void closeOverlay() {
        client.setOverlay(null);
    }
}
