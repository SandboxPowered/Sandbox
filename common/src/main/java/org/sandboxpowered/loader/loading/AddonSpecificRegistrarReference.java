package org.sandboxpowered.loader.loading;

import org.sandboxpowered.api.addon.AddonInfo;
import org.sandboxpowered.api.block.BaseBlock;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.item.BaseBlockItem;
import org.sandboxpowered.api.item.BlockItem;
import org.sandboxpowered.api.registry.Registrar;
import org.sandboxpowered.api.registry.Registry;
import org.sandboxpowered.api.resources.ResourceService;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.internal.AddonSpec;

import java.util.Optional;

public class AddonSpecificRegistrarReference implements Registrar {
    private final AddonSpec spec;
    private final SandboxLoader loader;

    public AddonSpecificRegistrarReference(AddonSpec spec, SandboxLoader loader) {
        this.spec = spec;
        this.loader = loader;
    }

    @Override
    public AddonInfo getSourceAddon() {
        return spec;
    }

    @Override
    public <T extends Content<T>> Registry.Entry<T> getEntry(Identity identity, Class<T> tClass) {
        return Registry.getRegistryFromType(tClass).get(identity);
    }

    @Override
    public <T extends Content<T>> Registry.Entry<T> getEntry(Identity identity, Registry<T> registry) {
        return registry.get(identity);
    }

    @Override
    public <T extends Content<T>> Registry.Entry<T> register(T content) {
        return loader.register(content);
    }

    @Override
    public <T extends Service> Optional<T> getRegistrarService(Class<T> tClass) {
        if (tClass == ResourceService.class)
            return Optional.of((T) loader.getResourceService().getServiceFor(getSourceAddon()));
        return Optional.empty();
    }
}