package org.sandboxpowered.loader.inject;

import com.google.inject.Singleton;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;
import org.sandboxpowered.api.events.EventHandlerFactory;
import org.sandboxpowered.api.inject.FactoryProvider;
import org.sandboxpowered.api.item.tool.ToolMaterials;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.eventhandler.ResettableEventHandler;
import org.sandboxpowered.loader.Wrappers;
import org.sandboxpowered.loader.inject.factory.IdentityFactory;

import java.util.Map;
import java.util.Objects;

@Singleton
public class SandboxFactoryProvider implements FactoryProvider {
    private final Map<Class<?>, Object> factories = new Object2ObjectOpenHashMap<>();

    public SandboxFactoryProvider() {
        registerDefaultFactories();
    }

    @Override
    public <T> T get(Class<T> factoryClass) throws FactoryNotFoundException {
        final Object factory = this.factories.get(factoryClass);
        if (factory == null)
            throw new FactoryNotFoundException(String.format("Type '%s' has no factory.", factoryClass));
        return (T) factory;
    }

    public <T> SandboxFactoryProvider registerFactory(Class<T> factoryClass, T factory) {
        Objects.requireNonNull(factory, "factory");
        factories.put(factoryClass, factory);
        return this;
    }

    public void registerDefaultFactories() {
        registerFactory(Identity.Factory.class, new IdentityFactory());
        registerFactory(ToolMaterials.Factory.class, material -> {
            switch (material) {
                case "stone":
                    return Wrappers.TOOL_MATERIAL.toSandbox(Tiers.STONE);
                case "iron":
                    return Wrappers.TOOL_MATERIAL.toSandbox(Tiers.IRON);
                case "gold":
                    return Wrappers.TOOL_MATERIAL.toSandbox(Tiers.GOLD);
                case "diamond":
                    return Wrappers.TOOL_MATERIAL.toSandbox(Tiers.DIAMOND);
                case "netherite":
                    return Wrappers.TOOL_MATERIAL.toSandbox(Tiers.NETHERITE);
                case "wood":
                default:
                    return Wrappers.TOOL_MATERIAL.toSandbox(Tiers.WOOD);
            }
        });
        registerFactory(EventHandlerFactory.class, ResettableEventHandler::new);
    }
}