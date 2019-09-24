package org.sandboxpowered.sandbox.fabric.mixin.impl.item;

import org.sandboxpowered.sandbox.api.item.Item;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(net.minecraft.item.Item.class)
@Implements(@Interface(iface = Item.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public class MixinItem {

}