package org.sandboxpowered.loader.inject.factory;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.sandboxpowered.api.item.attribute.Attribute;
import org.sandboxpowered.api.item.attribute.Attributes;
import org.sandboxpowered.loader.Wrappers;

public class AttributeFactory implements Attributes.BuiltinAttributeFactory {
    @Override
    public Attribute from(String id) {
        return Wrappers.ATTRIBUTE.toSandbox(Registry.ATTRIBUTE.get(new ResourceLocation(id)));
    }
}
