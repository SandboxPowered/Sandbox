package com.hrznstudio.sandbox.mixin.impl.entity;

import com.hrznstudio.sandbox.api.entity.Entity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(net.minecraft.block.Block.class)
@Implements(@Interface(iface = Entity.class, prefix = "sbx$"))
@Unique
public abstract class MixinEntity {

}