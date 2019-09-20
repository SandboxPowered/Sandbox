package com.hrznstudio.sandbox.mixin.impl.entity;

import com.hrznstudio.sandbox.api.Registries;
import com.hrznstudio.sandbox.api.container.ContainerFactory;
import com.hrznstudio.sandbox.api.container.IContainer;
import com.hrznstudio.sandbox.api.entity.player.Player;
import com.hrznstudio.sandbox.api.util.Identity;
import com.hrznstudio.sandbox.api.util.Mono;
import com.hrznstudio.sandbox.api.util.nbt.CompoundTag;
import com.hrznstudio.sandbox.container.ContainerWrapper;
import com.hrznstudio.sandbox.network.ContainerOpenPacket;
import com.hrznstudio.sandbox.network.NetworkManager;
import com.hrznstudio.sandbox.util.wrapper.V2SInventory;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity implements Player {
    @Shadow
    private int containerSyncId;

    @Shadow
    protected abstract void incrementContainerSyncId();

    public MixinServerPlayerEntity(World world_1, GameProfile gameProfile_1) {
        super(world_1, gameProfile_1);
    }

    @Override
    public void openContainer(Identity id, Mono<CompoundTag> dataMono) {
        incrementContainerSyncId();
        int syncId = containerSyncId;
        CompoundTag data = dataMono.orElseGet(CompoundTag::create);

        ContainerOpenPacket packet = new ContainerOpenPacket(id, syncId, data);
        NetworkManager.sendTo(packet, this);

        ContainerFactory factory = Registries.CONTAINER.get(id);
        IContainer container = factory.create(id, new V2SInventory(inventory), data);

        this.container = new ContainerWrapper(null, syncId, container);
        this.container.addListener((ServerPlayerEntity) (Object) this);
    }
}