package com.hrznstudio.sandbox.mixin.common.block;

import com.hrznstudio.sandbox.api.block.entity.IBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntityType.class)
@Implements(@Interface(iface = IBlockEntity.Type.class, prefix = "sbx$"))
@Unique
public class MixinBlockEntityType {

}
