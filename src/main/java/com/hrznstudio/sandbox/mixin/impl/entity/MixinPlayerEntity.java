package com.hrznstudio.sandbox.mixin.impl.entity;

import com.hrznstudio.sandbox.api.entity.player.Player;
import com.hrznstudio.sandbox.api.util.text.Text;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements Player {
    public MixinPlayerEntity(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Shadow
    public abstract void addChatMessage(net.minecraft.text.Text text_1, boolean boolean_1);

    @Override
    public void sendChatMessage(Text text) {
        this.addChatMessage(WrappingUtil.convert(text), false);
    }

    @Override
    public void sendOverlayMessage(Text text) {
        this.addChatMessage(WrappingUtil.convert(text), true);
    }
}
