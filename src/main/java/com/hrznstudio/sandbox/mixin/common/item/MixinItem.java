package com.hrznstudio.sandbox.mixin.common.item;

import com.hrznstudio.sandbox.api.item.IItem;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(net.minecraft.item.Item.class)
@Implements(@Interface(iface = IItem.class, prefix = "sbx$"))
@Unique
public class MixinItem {

}