package org.sandboxpowered.sandbox.fabric.mixin.impl.block;

import net.minecraft.block.entity.BlockEntityType;
import org.sandboxpowered.api.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntityType.class)
@Implements(@Interface(iface = BlockEntity.Type.class, prefix = "sbx$", remap = Interface.Remap.NONE))
@Unique
public class MixinBlockEntityType {

}
