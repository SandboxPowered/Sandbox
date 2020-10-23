package org.sandboxpowered.example;

import org.sandboxpowered.api.block.entity.BaseBlockEntity;
import org.sandboxpowered.api.util.nbt.ReadableCompoundTag;
import org.sandboxpowered.api.util.nbt.WritableCompoundTag;

public class PipeBlockEntity extends BaseBlockEntity {
    public PipeBlockEntity(Type<?> type) {
        super(type);
    }

    @Override
    public void read(ReadableCompoundTag tag) {

    }

    @Override
    public void write(WritableCompoundTag tag) {

    }
}
