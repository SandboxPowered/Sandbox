package org.sandboxpowered.example;

import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.block.BaseBlock;
import org.sandboxpowered.api.ecs.EntityBlueprint;
import org.sandboxpowered.api.ecs.component.BlockEntityPositionComponent;
import org.sandboxpowered.api.world.WorldReader;

public class TestBlockEntity extends BaseBlock {
    public TestBlockEntity(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable EntityBlueprint createBlockEntityBlueprint(WorldReader reader) {
        return EntityBlueprint.builder()
                .add(BlockEntityPositionComponent.class)
                .build(reader);
    }
}
