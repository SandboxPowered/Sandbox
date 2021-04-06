package org.sandboxpowered.loader.mixin.injection.item.attribute;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AttributeModifier.class)
@Implements(@Interface(iface = org.sandboxpowered.api.item.attribute.Attribute.Modifier.class, prefix = "mods$", remap = Interface.Remap.NONE))
@Unique
public class MixinAttributeModifier {

}
