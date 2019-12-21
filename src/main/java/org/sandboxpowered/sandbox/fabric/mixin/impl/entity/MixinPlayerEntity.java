package org.sandboxpowered.sandbox.fabric.mixin.impl.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.sandboxpowered.sandbox.api.entity.player.PlayerEntity;
import org.sandboxpowered.sandbox.api.util.Identity;
import org.sandboxpowered.sandbox.api.util.Mono;
import org.sandboxpowered.sandbox.api.util.nbt.CompoundTag;
import org.sandboxpowered.sandbox.api.util.text.Text;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.entity.player.PlayerEntity.class)
@Implements(@Interface(iface = PlayerEntity.class, prefix = "sbx$"))
public abstract class MixinPlayerEntity extends LivingEntity {
    public MixinPlayerEntity(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Shadow
    public abstract void addChatMessage(net.minecraft.text.Text text_1, boolean boolean_1);

    public void sbx$sendChatMessage(Text text) {
        this.addChatMessage(WrappingUtil.convert(text), false);
    }

    public void sbx$sendOverlayMessage(Text text) {
        this.addChatMessage(WrappingUtil.convert(text), true);
    }

    public void sbx$openContainer(Identity id, Mono<CompoundTag> data) {
        // NO-OP
    }
}