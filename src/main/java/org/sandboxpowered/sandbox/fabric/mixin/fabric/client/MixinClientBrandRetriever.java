package org.sandboxpowered.sandbox.fabric.mixin.fabric.client;

import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientBrandRetriever.class)
public class MixinClientBrandRetriever {

    /**
     * @reason Replace client branding
     * @author Coded
     */
    @Overwrite
    public static String getClientModName() {
        return "Sandbox";
    }
}
