package org.sandboxpowered.sandbox.fabric.mixin.impl.shape;

import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import org.sandboxpowered.api.shape.Shape;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Box.class)
@Implements(@Interface(iface = org.sandboxpowered.api.shape.Box.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public class MixinBox {

}
