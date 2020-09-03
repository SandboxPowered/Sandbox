package org.sandboxpowered.sandbox.fabric.service.rendering;

import org.sandboxpowered.api.client.rendering.ui.DynamicRenderer;
import org.sandboxpowered.api.client.rendering.ui.TextRenderer;
import org.sandboxpowered.api.util.math.MatrixStack;
import org.sandboxpowered.api.util.text.Text;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

public class FabricTextRenderer implements TextRenderer {
    private final net.minecraft.client.font.TextRenderer vanillaRenderer;
    private final FabricDynamicRenderer renderer;

    public FabricTextRenderer(net.minecraft.client.font.TextRenderer vanillaRenderer, FabricDynamicRenderer renderer) {
        this.vanillaRenderer = vanillaRenderer;
        this.renderer = renderer;
    }

    @Override
    public void renderText(MatrixStack stack, Text text, Alignment alignment, int posX, int posY, int color) {
        int offset = 0;
        if (alignment != Alignment.LEFT)
            offset = vanillaRenderer.getWidth(WrappingUtil.convert(text));
        if (alignment == Alignment.CENTER && offset > 0)
            offset /= 2;
        int x = posX + offset;
        vanillaRenderer.draw(WrappingUtil.convert(stack), WrappingUtil.convert(text), x, posY, color);
    }
}