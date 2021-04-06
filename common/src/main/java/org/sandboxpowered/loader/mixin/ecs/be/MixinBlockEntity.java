package org.sandboxpowered.loader.mixin.ecs.be;

import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntity.class)
public class MixinBlockEntity {
    private static int totalEntities = 0;

    private final int sandbox_ecsId = totalEntities++;
}
