package org.sandboxpowered.sandbox.fabric.mixin.impl.entity;

import org.sandboxpowered.sandbox.api.entity.Entity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(net.minecraft.entity.Entity.class)
@Implements(@Interface(iface = Entity.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public abstract class MixinEntity {
}