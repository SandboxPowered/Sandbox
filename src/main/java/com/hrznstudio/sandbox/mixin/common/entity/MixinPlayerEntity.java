package com.hrznstudio.sandbox.mixin.common.entity;

import com.hrznstudio.sandbox.api.entity.player.Player;
import com.hrznstudio.sandbox.api.event.ItemEvent;
import com.hrznstudio.sandbox.api.util.text.Text;
import com.hrznstudio.sandbox.server.SandboxServer;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Predicate;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements Player {
    @Shadow
    @Final
    public PlayerAbilities abilities;
    @Shadow
    @Final
    public PlayerInventory inventory;

    @Shadow public abstract void addChatMessage(net.minecraft.text.Text text_1, boolean boolean_1);

    public MixinPlayerEntity(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Override
    public void sendChatMessage(Text text) {
        this.addChatMessage(WrappingUtil.convert(text), false);
    }

    @Override
    public void sendOverlayMessage(Text text) {
        this.addChatMessage(WrappingUtil.convert(text), true);
    }

    /**
     * @author Coded
     */
    @Overwrite
    public ItemStack getArrowType(ItemStack weapon) {
        ItemEvent.GetArrowType event = SandboxServer.INSTANCE.dispatcher.publish(new ItemEvent.GetArrowType(
                WrappingUtil.cast(weapon, com.hrznstudio.sandbox.api.item.ItemStack.class),
                WrappingUtil.cast(getVanillaArrowType(weapon), com.hrznstudio.sandbox.api.item.ItemStack.class)
        ));
        return WrappingUtil.convert(event.getArrow());
    }

    private ItemStack getVanillaArrowType(ItemStack weapon) {
        if (!(weapon.getItem() instanceof RangedWeaponItem)) {
            return ItemStack.EMPTY;
        } else {
            Predicate<ItemStack> predicate_1 = ((RangedWeaponItem) weapon.getItem()).getHeldProjectiles();
            ItemStack itemStack_2 = RangedWeaponItem.getHeldProjectile(this, predicate_1);
            if (!itemStack_2.isEmpty()) {
                return itemStack_2;
            } else {
                predicate_1 = ((RangedWeaponItem) weapon.getItem()).getProjectiles();

                for (int int_1 = 0; int_1 < this.inventory.getInvSize(); ++int_1) {
                    ItemStack itemStack_3 = this.inventory.getInvStack(int_1);
                    if (predicate_1.test(itemStack_3)) {
                        return itemStack_3;
                    }
                }

                return abilities.creativeMode ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
            }
        }
    }
}
