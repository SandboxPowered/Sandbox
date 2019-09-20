package com.hrznstudio.sandbox.network;

import com.hrznstudio.sandbox.api.Registries;
import com.hrznstudio.sandbox.api.client.screen.ContainerScreen;
import com.hrznstudio.sandbox.api.container.ContainerFactory;
import com.hrznstudio.sandbox.api.container.IContainer;
import com.hrznstudio.sandbox.api.util.Identity;
import com.hrznstudio.sandbox.api.util.nbt.CompoundTag;
import com.hrznstudio.sandbox.container.ContainerWrapper;
import com.hrznstudio.sandbox.util.Log;
import com.hrznstudio.sandbox.util.WrappingUtil;
import com.hrznstudio.sandbox.util.wrapper.V2SInventory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.PacketByteBuf;

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
        MinecraftClient.getInstance().execute(() -> {
            ContainerFactory factory = Registries.CONTAINER.get(id);
            if (factory == null) {
                Log.error("No Container factory found for " + id.toString() + "!");
                return;
            }
            IContainer container = factory.create(id, new V2SInventory(MinecraftClient.getInstance().player.inventory), data);
            if(container!=null) {
                ContainerScreen screen = factory.create(container);
                MinecraftClient.getInstance().player.container = new ContainerWrapper(null, syncId, container);
                MinecraftClient.getInstance().openScreen(WrappingUtil.convert(screen));
            }
        });
    }
}