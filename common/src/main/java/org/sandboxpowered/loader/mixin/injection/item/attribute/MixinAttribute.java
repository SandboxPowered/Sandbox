package org.sandboxpowered.loader.mixin.injection.item.attribute;

import net.minecraft.world.entity.ai.attributes.Attribute;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Attribute.class)
@Implements(@Interface(iface = org.sandboxpowered.api.item.attribute.Attribute.class, prefix = "attr$", remap = Interface.Remap.NONE))
@Unique
public class MixinAttribute {

}
