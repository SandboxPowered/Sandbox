package org.sandboxpowered.loader.loading;

import org.sandboxpowered.api.addon.AddonInfo;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.registry.Registrar;
import org.sandboxpowered.api.registry.Registry;
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
        return Registry.getRegistryFromType(content.getContentType()).register(content);
    }

    @Override
    public <T extends Service> Optional<T> getRegistrarService(Class<T> tClass) {
        return Optional.empty();
    }
}