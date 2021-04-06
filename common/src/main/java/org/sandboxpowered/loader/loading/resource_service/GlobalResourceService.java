package org.sandboxpowered.loader.loading.resource_service;

import org.sandboxpowered.api.addon.AddonInfo;
import org.sandboxpowered.api.block.Blocks;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.item.Items;
import org.sandboxpowered.api.registry.Registry;
import org.sandboxpowered.api.resources.ResourceConstants;
import org.sandboxpowered.api.resources.ResourceMaterial;
import org.sandboxpowered.api.resources.ResourceService;
import org.sandboxpowered.api.resources.ResourceType;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.loader.loading.AddonSpecificResourceRegistrar;

import java.util.*;

import static org.sandboxpowered.api.resources.ResourceConstants.*;
import static org.sandboxpowered.api.resources.ResourceConstants.DUST;
import static org.sandboxpowered.api.resources.ResourceConstants.GEM;

public class GlobalResourceService {
    private final Map<AddonInfo, AddonSpecificResourceRegistrar> serviceMap = new LinkedHashMap<>();
    private final Map<ResourceMaterial, Map<ResourceType<?>, ResourceImpl<?>>> resourceMap = new IdentityHashMap<>();
    private final PrioritySortOrder sortOrder = new PrioritySortOrder();

    public void initVanillaContent() {
        add(IRON, INGOT, Items.IRON_INGOT);
        add(IRON, NUGGET, Items.IRON_NUGGET);
        add(IRON, ORE, Blocks.IRON_ORE);
        add(IRON, BLOCK, Blocks.IRON_BLOCK);

        add(GOLD, INGOT, Items.GOLD_INGOT);
        add(GOLD, NUGGET, Items.GOLD_NUGGET);
        add(GOLD, ORE, Blocks.GOLD_ORE);
        add(GOLD, NETHER_ORE, Blocks.NETHER_GOLD_ORE);
        add(GOLD, BLOCK, Blocks.GOLD_BLOCK);

        add(DIAMOND, GEM, Items.DIAMOND);
        add(DIAMOND, ORE, Blocks.DIAMOND_ORE);
        add(DIAMOND, BLOCK, Blocks.DIAMOND_BLOCK);

        add(EMERALD, GEM, Items.EMERALD);
        add(EMERALD, ORE, Blocks.EMERALD_ORE);
        add(EMERALD, BLOCK, Blocks.EMERALD_BLOCK);
        
        add(GLOWSTONE, DUST, Items.GLOWSTONE_DUST);
        add(GLOWSTONE, BLOCK, Blocks.GLOWSTONE);

        add(COAL, GEM, Items.COAL);
        add(COAL, ORE, Blocks.COAL_ORE);
        add(COAL, BLOCK, Blocks.COAL_BLOCK);

        add(OBSIDIAN, BLOCK, Blocks.OBSIDIAN);

        add(NETHERITE, INGOT, Items.NETHERITE_INGOT);
        add(NETHERITE, SCRAP, Items.NETHERITE_SCRAP);
        add(NETHERITE, NETHER_ORE, Blocks.ANCIENT_DEBRIS);
        add(NETHERITE, BLOCK, Blocks.NETHERITE_BLOCK);

        add(REDSTONE, DUST, Items.REDSTONE);
        add(REDSTONE, ORE, Blocks.REDSTONE_ORE);
        add(REDSTONE, BLOCK, Blocks.REDSTONE_BLOCK);

        add(LAPIS, GEM, Items.LAPIS_LAZULI);
        add(LAPIS, ORE, Blocks.LAPIS_ORE);
        add(LAPIS, BLOCK, Blocks.LAPIS_BLOCK);

        add(QUARTZ, GEM, Items.QUARTZ);
        add(QUARTZ, NETHER_ORE, Blocks.NETHER_QUARTZ_ORE);
        add(QUARTZ, BLOCK, Blocks.QUARTZ_BLOCK);
    }

    public void clear() {
        resourceMap.clear();
        serviceMap.clear();
    }

    public ResourceService getServiceFor(AddonInfo info) {
        return serviceMap.computeIfAbsent(info, i -> new AddonSpecificResourceRegistrar(info, this));
    }

    public Map<ResourceMaterial, Map<ResourceType<?>, ResourceImpl<?>>> getResourceMap() {
        return resourceMap;
    }

    public <C extends Content<C>> void add(ResourceMaterial material, ResourceType<C> type, Registry.Entry<C> variant) {
        add(material, type, variant.get());
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
            List<String> namespaces = Collections.singletonList("minecraft");
            int comparison = Integer.compare(
                    getPriority(id1.getNamespace(), namespaces),
                    getPriority(id2.getNamespace(), namespaces)
            );
            return comparison != 0 ? comparison : id1.toString().compareTo(id2.toString());
        }
    }
}
