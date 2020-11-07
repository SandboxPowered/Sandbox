package org.sandboxpowered.loader.forge.mixin.brand;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.BrandingControl;
import net.minecraftforge.fml.ForgeI18n;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.versions.forge.ForgeVersion;
import net.minecraftforge.versions.mcp.MCPVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(BrandingControl.class)
public class MixinBrandingControl {
    @Shadow
    private static List<String> brandings;

    @Shadow
    private static List<String> brandingsNoMC;

    /**
     * @author Coded
     * @reason Add sandbox to the brandings list
     */
    @Overwrite
    private static void computeBranding() {
        if (brandings == null) {
            ImmutableList.Builder<String> brd = ImmutableList.builder();
            brd.add("Forge " + ForgeVersion.getVersion());
            brd.add("Sandbox Forge " + ModList.get().getModContainerById("sandbox-forge").map(container -> container.getModInfo().getVersion().toString()).orElse("MISSING") + " | API 0.5.0");
            brd.add("Minecraft " + MCPVersion.getMCVersion());
            int tModCount = ModList.get().size();
            brd.add(ForgeI18n.parseMessage("fml.menu.loadingmods", tModCount));
            brandings = brd.build();
            brandingsNoMC = brandings.subList(2, brandings.size());
        }
    }
}
