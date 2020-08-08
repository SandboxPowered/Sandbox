package org.sandboxpowered.sandbox.fabric.mixin.impl.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.sandboxpowered.api.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerEntity.class)
@Implements(@Interface(iface = PlayerEntity.class, prefix = "sbx$", remap = Interface.Remap.NONE))
public abstract class MixinServerPlayerEntity extends net.minecraft.entity.player.PlayerEntity {
    public MixinServerPlayerEntity(World world, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(world, blockPos, f, gameProfile);
    }
//
//    public void sbx$openContainer(Identity id, CompoundTag dataMono) {
//        incrementContainerSyncId();
//        int syncId = containerSyncId;
//        CompoundTag data = dataMono == null ? CompoundTag.create() : dataMono;
//
//        ContainerOpenPacket packet = new ContainerOpenPacket(id, syncId, data);
//        NetworkManager.sendTo(packet, this);
//
//        Registries.CONTAINER.get(id).ifPresent(factory -> {
//            Container container = factory.create(id, new V2SInventory(inventory), data);
//
//            this.container = new ContainerWrapper(null, syncId, container);
//            this.container.addListener((ServerPlayerEntity) (Object) this);
//        });
//    }
}