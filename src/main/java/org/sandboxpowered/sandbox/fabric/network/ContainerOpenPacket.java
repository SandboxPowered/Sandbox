package org.sandboxpowered.sandbox.fabric.network;

import net.minecraft.network.PacketByteBuf;
import org.sandboxpowered.api.util.Identity;
import org.sandboxpowered.api.util.nbt.CompoundTag;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

public class ContainerOpenPacket implements Packet {
    private Identity id;
    private int syncId;
    private CompoundTag data;

    public ContainerOpenPacket(Identity id, int syncId, CompoundTag data) {
        this.id = id;
        this.syncId = syncId;
        this.data = data;
    }

    public ContainerOpenPacket() {
    }

    @Override
    public void read(PacketByteBuf buf) {
        id = (Identity) buf.readIdentifier();
        syncId = buf.readInt();
        data = (CompoundTag) buf.readCompoundTag();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(WrappingUtil.convert(id));
        buf.writeInt(syncId);
        buf.writeCompoundTag((net.minecraft.nbt.CompoundTag) data);
    }

    @Override
    public void apply() {
//        MinecraftClient.getInstance().execute(() -> {
//            Registries.CONTAINER.get(id).ifPresent(factory -> {
//                Container container = factory.create(id, new V2SInventory(MinecraftClient.getInstance().player.inventory), data);
//                if (container != null) {
//                    ContainerScreen screen = factory.create(container);
//                    MinecraftClient.getInstance().player.container = new ContainerWrapper(null, syncId, container);
//                    MinecraftClient.getInstance().openScreen(WrappingUtil.convert(screen));
//                }
//            }, () -> Log.error("No Container factory found for " + id.toString() + "!"));
//        });
    }
}