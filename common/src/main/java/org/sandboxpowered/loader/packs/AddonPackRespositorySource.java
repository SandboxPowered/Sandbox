package org.sandboxpowered.loader.packs;

import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.FolderPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import org.sandboxpowered.internal.AddonSpec;
import org.sandboxpowered.loader.loading.SandboxLoader;
import org.sandboxpowered.loader.platform.Platform;
import org.sandboxpowered.loader.platform.SandboxPlatform;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AddonPackRespositorySource implements RepositorySource {
    @Override
    public void loadPacks(Consumer<Pack> consumer, Pack.PackConstructor packConstructor) {
        SandboxPlatform platform = Platform.getPlatform();
        if (platform.isLoaded()) {
            SandboxLoader loader = platform.getLoader();
            List<PackResources> packs = new ArrayList<>();
            loader.getAllAddons().forEach((addonSpec, addon) -> {
                try {
                    packs.add(createAddonPack(addonSpec, new File(addonSpec.getPath().toURI())));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            });
            Pack pack = Pack.create("Addon Resources", true, () -> new MergedSandboxPack("addon_resources", "Addon Resources", packs), packConstructor, Pack.Position.BOTTOM, PackSource.DEFAULT);
            if (pack != null)
                consumer.accept(pack);
        }
    }

    private PackResources createAddonPack(AddonSpec spec, File file) {
        return new AddonPackWrapper(spec, file.isDirectory() ? new FolderPackResources(file) : new FilePackResources(file));
    }
}