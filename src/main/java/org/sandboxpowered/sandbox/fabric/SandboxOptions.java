package org.sandboxpowered.sandbox.fabric;

import net.minecraft.client.options.CyclingOption;
import net.minecraft.text.TranslatableText;

public class SandboxOptions {
    public static WorldBorder border = WorldBorder.VANILLA;
    public static final CyclingOption WORLD_BORDER = new CyclingOption("options.sandbox.worldborder", (gameOptions, integer) -> {
        switch (border) {
            case VANILLA:
                border = WorldBorder.LINES;
                break;
            case LINES:
                border = WorldBorder.GRID;
                break;
            case GRID:
                border = WorldBorder.DOTS;
                break;
            case DOTS:
                border = WorldBorder.VANILLA;
                break;
        }
    }, (gameOptions, cyclingOption) -> cyclingOption.method_30501(new TranslatableText(border.getTranslation())));

    public static void load() {

    }

    public static void save() {

    }

    public enum WorldBorder {
        VANILLA("options.sandbox.worldborder.vanilla"),
        LINES("options.sandbox.worldborder.lines"),
        GRID("options.sandbox.worldborder.grid"),
        DOTS("options.sandbox.worldborder.dots");

        private final String translation;

        WorldBorder(String translation) {
            this.translation = translation;
        }

        public String getTranslation() {
            return translation;
        }
    }
}