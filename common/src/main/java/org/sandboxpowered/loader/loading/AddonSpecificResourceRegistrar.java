package org.sandboxpowered.loader.loading;

import org.sandboxpowered.api.addon.AddonInfo;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.resources.ResourceMaterial;
import org.sandboxpowered.api.resources.ResourceService;
import org.sandboxpowered.api.resources.ResourceType;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.internal.AddonSpec;
import org.sandboxpowered.loader.loading.resource_service.GlobalResourceService;

import java.util.function.Supplier;

public class AddonSpecificResourceRegistrar implements ResourceService {
    private final AddonInfo spec;
    private final GlobalResourceService resourceService;

    public AddonSpecificResourceRegistrar(AddonInfo spec, GlobalResourceService resourceService) {
        this.spec = spec;
        this.resourceService = resourceService;
    }

    @Override
    public void add(ResourceMaterial material, ResourceType<?>... types) {
        for (ResourceType<?> type : types) {
            add(material, type);
        }
    }

    @Override
    public <C extends Content<C>> void add(ResourceMaterial material, ResourceType<C> type) {
        add(material, type, type.createContent(material));
    }

    @Override
    public <C extends Content<C>> void add(ResourceMaterial material, ResourceType<C> type, Supplier<C> supplier) {
        add(material, type, supplier.get());
    }

    @Override
    public <C extends Content<C>> void add(ResourceMaterial material, ResourceType<C> type, C content) {
        resourceService.add(material, type, content.setIdentity(Identity.of(spec.getId(), String.format("%s_%s", material.toString(), type.toString()))));
    }

    @Override
    public <C extends Content<C>> boolean contains(ResourceMaterial material, ResourceType<C> type) {
        return resourceService.contains(material, type);
    }
}
