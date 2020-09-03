package org.sandboxpowered.sandbox.fabric.service.rendering;

import net.minecraft.client.MinecraftClient;
import org.sandboxpowered.api.client.rendering.RenderPipeline;
import org.sandboxpowered.api.client.rendering.ui.TextRenderer;

public class UniversalRenderPipeline implements RenderPipeline {

    private FabricDynamicRenderer renderer = new FabricDynamicRenderer();
    private FabricTextRenderer text;

    @Override
    public FabricDynamicRenderer getDynamicRenderer() {
        return renderer;
    }

    @Override
    public TextRenderer getTextRenderer() {
        if (text == null) {
            text = new FabricTextRenderer(MinecraftClient.getInstance().textRenderer, renderer);
        }
        return text;
    }
}