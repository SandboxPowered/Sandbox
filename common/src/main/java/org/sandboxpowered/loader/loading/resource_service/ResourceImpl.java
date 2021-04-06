package org.sandboxpowered.loader.loading.resource_service;

import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.resources.Resource;
import org.sandboxpowered.api.resources.ResourceMaterial;
import org.sandboxpowered.api.resources.ResourceType;

import java.util.*;

public class ResourceImpl<C extends Content<C>> implements Resource<C> {
    private final SortedSet<C> variants;
    private final ResourceMaterial resourceMaterial;
    private final ResourceType<C> form;

    public ResourceImpl(ResourceMaterial resourceMaterial, ResourceType<C> form, Comparator<Content<?>> comparator) {
        this.variants = new TreeSet<>(comparator);
        this.resourceMaterial = resourceMaterial;
        this.form = form;
    }

    @Override
    public C get() {
        return variants.first();
    }

    @Override
    public Set<C> getVariants() {
        return Collections.unmodifiableSet(variants);
    }

    @Override
    public ResourceMaterial getMaterial() {
        return resourceMaterial;
    }

    @Override
    public ResourceType<C> getType() {
        return form;
    }

    @Override
    public String toString() {
        return String.format("Resource %s %s: %s", getMaterial(), getType(), get());
    }

    public void addVariant(C variant) {
        variants.add(variant);
    }
}