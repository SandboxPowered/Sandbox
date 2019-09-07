package com.hrznstudio.sandbox.mixin.impl.item;

import com.hrznstudio.sandbox.api.item.IItem;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(net.minecraft.item.Item.class)
@Implements(@Interface(iface = IItem.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public class MixinItem {

}