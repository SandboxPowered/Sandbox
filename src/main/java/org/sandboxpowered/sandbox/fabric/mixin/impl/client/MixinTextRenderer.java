package org.sandboxpowered.sandbox.fabric.mixin.impl.client;

import net.minecraft.client.font.TextRenderer;
import org.apache.commons.lang3.ArrayUtils;
import org.sandboxpowered.sandbox.api.client.TextRenderer.Alignment;
import org.sandboxpowered.sandbox.api.client.TextRenderer.Option;
import org.spongepowered.asm.mixin.*;

@Mixin(TextRenderer.class)
@Implements(@Interface(iface = org.sandboxpowered.sandbox.api.client.TextRenderer.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinTextRenderer {
    @Shadow
    @Final
    public int fontHeight;

    @Shadow
    public abstract int getStringWidth(String string_1);

    @Shadow
    public abstract String trimToWidth(String string_1, int int_1, boolean boolean_1);

    @Shadow
    public abstract int drawWithShadow(String string_1, float float_1, float float_2, int int_1);

    @Shadow
    public abstract int draw(String string_1, float float_1, float float_2, int int_1);

    public int sbx$getTextWidth(String text) {
        return getStringWidth(text);
    }

    public int sbx$getFontHeight() {
        return fontHeight;
    }

    public void sbx$draw(String text, float x, float y, int color, Alignment alignment, Option... options) {
        if (alignment == Alignment.RIGHT) {
            x -= sbx$getTextWidth(text);
        } else if (alignment == Alignment.CENTERED) {
            x -= sbx$getTextWidth(text) / 2f;
        }
        boolean shadow = ArrayUtils.contains(options, Option.SHADOW);
        if (shadow)
            drawWithShadow(text, x, y, color);
        else
            draw(text, x, y, color);
    }

    public String sbx$trim(String text, int width, boolean reverse) {
        return trimToWidth(text, width, reverse);
    }
}