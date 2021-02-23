package org.sandboxpowered.loader.inject.factory;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import org.sandboxpowered.api.item.attribute.Attribute;
import org.sandboxpowered.loader.Wrappers;
import org.sandboxpowered.loader.util.MojangUtil;

import java.util.UUID;

public class AttributeModifierFactory implements Attribute.ModifierFactory {
    @Override
    public Attribute.Modifier create(UUID attribute, String name, double value, Attribute.Operation operation) {
        attribute = MojangUtil.checkMojangity(attribute, Item.BASE_ATTACK_DAMAGE_UUID);
        attribute = MojangUtil.checkMojangity(attribute, Item.BASE_ATTACK_SPEED_UUID);
        return Wrappers.ATTRIBUTE_MODIFIER.toSandbox(new AttributeModifier(
                attribute, name, value, Wrappers.ATTRIBUTE_OPERATION.toVanilla(operation)
        ));
    }
}
