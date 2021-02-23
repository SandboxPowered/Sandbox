package org.sandboxpowered.loader.inject;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.inject.Singleton;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import org.checkerframework.checker.units.qual.A;
import org.sandboxpowered.api.block.Material;
import org.sandboxpowered.api.block.Materials;
import org.sandboxpowered.api.events.EventHandlerFactory;
import org.sandboxpowered.api.inject.FactoryProvider;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.item.attribute.Attribute;
import org.sandboxpowered.api.item.attribute.Attributes;
import org.sandboxpowered.api.item.tool.ToolMaterials;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.eventhandler.ResettableEventHandler;
import org.sandboxpowered.loader.Wrappers;
import org.sandboxpowered.loader.inject.factory.*;

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

    @CanIgnoreReturnValue
    public <T> SandboxFactoryProvider registerFactory(Class<T> factoryClass, T factory) {
        Objects.requireNonNull(factory, "factory");
        factories.put(factoryClass, factory);
        return this;
    }

    public void registerDefaultFactories() {
        registerFactory(Identity.Factory.class, new IdentityFactory());
        registerFactory(Position.Factory.class, new PositionFactory());
        registerFactory(Materials.Factory.class, new MaterialFactory());
        registerFactory(ItemStack.Factory.class, new ItemStackFactory());
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
        registerFactory(Attributes.BuiltinAttributeFactory.class, new AttributeFactory());
        registerFactory(Attribute.ModifierFactory.class, new AttributeModifierFactory());
        registerFactory(EventHandlerFactory.class, ResettableEventHandler::new);
    }
}