package org.sandboxpowered.sandbox.fabric.mixin.impl.entity;

import net.minecraft.entity.EntityType;
import org.sandboxpowered.sandbox.api.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityType.class)
public class MixinEntityType implements Entity.Type {

}
