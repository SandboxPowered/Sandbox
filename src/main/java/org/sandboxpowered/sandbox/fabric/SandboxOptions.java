package org.sandboxpowered.sandbox.fabric;

import net.minecraft.client.options.CyclingOption;
import net.minecraft.text.TranslatableText;

public class SandboxOptions {
    public static final CyclingOption WORLD_BORDER = new CyclingOption("options.sandbox.worldborder", (gameOptions, integer) -> {
        SandboxConfig.updateBorderType(SandboxConfig.worldBorder.getEnum(SandboxConfig.WorldBorder.class).next());
    }, (gameOptions, cyclingOption) -> cyclingOption.method_30501(new TranslatableText(SandboxConfig.worldBorder.getEnum(SandboxConfig.WorldBorder.class).getTranslation())));

}