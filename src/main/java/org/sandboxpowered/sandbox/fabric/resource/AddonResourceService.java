package org.sandboxpowered.sandbox.fabric.resource;

import org.sandboxpowered.api.addon.AddonInfo;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.resources.ResourceMaterial;
import org.sandboxpowered.api.resources.ResourceService;
import org.sandboxpowered.api.resources.ResourceType;
import org.sandboxpowered.api.util.Identity;

import java.util.function.Supplier;

public class AddonResourceService implements ResourceService {
    private final AddonInfo addon;
    private final GlobalResourceRegistrationService global;

    public AddonResourceService(AddonInfo addon, GlobalResourceRegistrationService global) {
        this.addon = addon;
        this.global = global;
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
        global.add(material, type, content.setIdentity(Identity.of(addon.getId(), material.toString() + "_" + type.toString())));
    }

    @Override
    public <C extends Content<C>> boolean contains(ResourceMaterial material, ResourceType<C> type) {
        return global.contains(material, type);
    }
}