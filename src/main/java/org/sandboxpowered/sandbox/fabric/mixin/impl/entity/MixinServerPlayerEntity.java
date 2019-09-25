package org.sandboxpowered.sandbox.fabric.mixin.impl.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.sandboxpowered.sandbox.api.Registries;
import org.sandboxpowered.sandbox.api.container.Container;
import org.sandboxpowered.sandbox.api.entity.player.PlayerEntity;
import org.sandboxpowered.sandbox.api.util.Identity;
import org.sandboxpowered.sandbox.api.util.Mono;
import org.sandboxpowered.sandbox.api.util.nbt.CompoundTag;
import org.sandboxpowered.sandbox.fabric.container.ContainerWrapper;
import org.sandboxpowered.sandbox.fabric.network.ContainerOpenPacket;
import org.sandboxpowered.sandbox.fabric.network.NetworkManager;
import org.sandboxpowered.sandbox.fabric.util.wrapper.V2SInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends net.minecraft.entity.player.PlayerEntity implements PlayerEntity {
    @Shadow
    private int containerSyncId;

    public MixinServerPlayerEntity(World world_1, GameProfile gameProfile_1) {
        super(world_1, gameProfile_1);
    }

    @Shadow
    protected abstract void incrementContainerSyncId();

    @Override
    public void openContainer(Identity id, Mono<CompoundTag> dataMono) {
        incrementContainerSyncId();
        int syncId = containerSyncId;
        CompoundTag data = dataMono.orElseGet(CompoundTag::create);

        ContainerOpenPacket packet = new ContainerOpenPacket(id, syncId, data);
        NetworkManager.sendTo(packet, this);

        Registries.CONTAINER.get(id).ifPresent(factory -> {
            Container container = factory.create(id, new V2SInventory(inventory), data);

            this.container = new ContainerWrapper(null, syncId, container);
            this.container.addListener((ServerPlayerEntity) (Object) this);
        });
    }
}