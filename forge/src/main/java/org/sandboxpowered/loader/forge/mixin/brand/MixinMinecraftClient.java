package org.sandboxpowered.loader.forge.mixin.brand;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Minecraft.class)
public class MixinMinecraftClient {

    /**
     * @reason Sandbox Client Branding
     * @author Coded
     */
    @Overwrite
    public String getVersionType() {
        return "Sandbox";
    }

    /**
     * @reason Is definitely modded
     * @author Coded
     */
    @Overwrite
    public boolean isProbablyModded() {
        return true;
    }
}