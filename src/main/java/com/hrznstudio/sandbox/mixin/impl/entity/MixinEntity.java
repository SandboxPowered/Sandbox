package com.hrznstudio.sandbox.mixin.impl.entity;

import com.hrznstudio.sandbox.api.entity.IEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
@Implements(@Interface(iface = IEntity.class, prefix = "sbx$"))
@Unique
public abstract class MixinEntity {
}