package org.sandboxpowered.loader.wrapper;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.sandboxpowered.api.ecs.component.Component;

import java.util.List;

public class WrappedBlockEntity extends BlockEntity {

    private List<Component> components;

    public WrappedBlockEntity(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
    }
}
