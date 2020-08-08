package org.sandboxpowered.sandbox.fabric;

import net.minecraft.client.options.CyclingOption;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.sandboxpowered.api.util.Identity;

public class SandboxOptions {
    public static WorldBorder border = WorldBorder.VANILLA;
    public static final CyclingOption WORLD_BORDER = new CyclingOption("options.sandbox.worldborder", (gameOptions, integer) -> {
        updateBorderType(border.next());
    }, (gameOptions, cyclingOption) -> cyclingOption.method_30501(new TranslatableText(border.getTranslation())));

    public static void updateBorderType(WorldBorder border) {
        SandboxOptions.border = border;
        save();
    }

    public static void load() {

    }

    public static void save() {

    }

    public enum WorldBorder {
        VANILLA("options.sandbox.worldborder.vanilla"),
        LINES("options.sandbox.worldborder.lines", new Identifier("sandbox", "textures/misc/lines.png")),
        GRID("options.sandbox.worldborder.grid", new Identifier("sandbox", "textures/misc/grid.png")),
        DOTS("options.sandbox.worldborder.dots", new Identifier("sandbox", "textures/misc/dot.png"));

        private static final WorldBorder[] VALUES = values();

        private final String translation;
        private final Identifier texture;

        WorldBorder(String translation, Identifier texture) {
            this.translation = translation;
            this.texture = texture;
        }

        WorldBorder(String translation) {
            this(translation, null);
        }

        public String getTranslation() {
            return translation;
        }

        public Identifier getTexture() {
            return texture;
        }

        public WorldBorder next() {
            int next = ordinal() + 1;
            if (next == VALUES.length)
                next = 0;
            return VALUES[next];
        }
    }
}