package org.sandboxpowered.sandbox.fabric.resource;

import org.sandboxpowered.api.addon.AddonInfo;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.resources.Resource;
import org.sandboxpowered.api.resources.ResourceMaterial;
import org.sandboxpowered.api.resources.ResourceService;
import org.sandboxpowered.api.resources.ResourceType;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.sandbox.fabric.SandboxConfig;

import java.util.*;

public class GlobalResourceRegistrationService {
    private final Map<AddonInfo, AddonResourceService> serviceMap = new LinkedHashMap<>();
    private final Map<ResourceMaterial, Map<ResourceType<?>, ResourceImpl<?>>> resourceMap = new IdentityHashMap<>();
    private final PrioritySortOrder sortOrder = new PrioritySortOrder();

    public void destroy() {
        serviceMap.clear();
    }

    public ResourceService getServiceFor(AddonInfo info) {
        return serviceMap.computeIfAbsent(info, i -> new AddonResourceService(info, this));
    }

    public Map<ResourceMaterial, Map<ResourceType<?>, ResourceImpl<?>>> getResourceMap() {
        return resourceMap;
    }

    public <C extends Content<C>> void add(ResourceMaterial material, ResourceType<C> type, C variant) {
        Map<ResourceType<?>, ResourceImpl<?>> map = resourceMap.computeIfAbsent(material, t -> new IdentityHashMap<>());
        ResourceImpl<C> resource = (ResourceImpl<C>) map.computeIfAbsent(type, t -> new ResourceImpl<>(material, type, sortOrder));
        resource.addVariant(variant);
    }

    public <C extends Content<C>> boolean contains(ResourceMaterial material, ResourceType<C> type) {
        Map<ResourceType<?>, ResourceImpl<?>> map = resourceMap.get(material);
        return map != null && map.containsKey(type);
    }

    public <C extends Content<C>> ResourceImpl<C> getResource(ResourceMaterial material, ResourceType<C> type) {
        if (!resourceMap.containsKey(material))
            return null;
        ResourceImpl<C> resource = (ResourceImpl<C>) resourceMap.get(material).get(type);
        return resource;
    }

    public static class PrioritySortOrder implements Comparator<Content<?>> {
        private static int getPriority(String namespace, List<String> namespaces) {
            int priority = namespaces.indexOf(namespace);
            return priority == -1 ? Integer.MAX_VALUE : priority;
        }

        @Override
        public int compare(Content<?> o1, Content<?> o2) {
            Identity id1 = o1.getIdentity();
            Identity id2 = o2.getIdentity();
            List<String> namespaces = SandboxConfig.priorityResourceNamespaces.get();
            int comparison = Integer.compare(
                    getPriority(id1.getNamespace(), namespaces),
                    getPriority(id2.getNamespace(), namespaces)
            );
            return comparison != 0 ? comparison : id1.toString().compareTo(id2.toString());
        }
    }
}