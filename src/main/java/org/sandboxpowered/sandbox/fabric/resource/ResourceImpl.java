package org.sandboxpowered.sandbox.fabric.resource;

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

    public Set<C> getVariants() {
        return Collections.unmodifiableSet(variants);
    }

    public ResourceMaterial getMaterial() {
        return resourceMaterial;
    }

    public ResourceType<C> getForm() {
        return form;
    }

    @Override
    public String toString() {
        return String.format("Resource %s %s: %s", getMaterial(), getForm(), get());
    }

    public void addVariant(C variant) {
        variants.add(variant);
    }
}
