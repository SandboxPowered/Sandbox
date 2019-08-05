package com.hrznstudio.sandbox.client.entity;

import com.hrznstudio.sandbox.maths.PointD;
import com.hrznstudio.sandbox.ragdoll.ragdolls.BaseRagdoll;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Arm;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class RagdollEntity extends LivingEntity {

    private final DefaultedList<ItemStack> handItems;
    private final DefaultedList<ItemStack> armorItems;

    public BaseRagdoll ragdoll;

    public double tempPosX;
    public double tempPosY;
    public double tempPosZ;
    public Box tempBoundingBox;

    private int remainingLife = 600;

    public RagdollEntity(EntityType<? extends RagdollEntity> entityType, World world) {
        super(entityType, world);
        this.handItems = DefaultedList.ofSize(2, ItemStack.EMPTY);
        this.armorItems = DefaultedList.ofSize(4, ItemStack.EMPTY);

        // To stop issues (TODO look to making just a large render box instead)
        this.ignoreCameraFrustum = true;
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return null;
    }

    public ItemStack getEquippedStack(EquipmentSlot var1) {
        switch (var1) {
            case MAINHAND:
                return this.handItems.get(0);
            case OFFHAND:
                return this.handItems.get(1);
            case FEET:
                return this.armorItems.get(0);
            case LEGS:
                return this.armorItems.get(1);
            case CHEST:
                return this.armorItems.get(2);
            case HEAD:
                return this.armorItems.get(3);
            default:
                return ItemStack.EMPTY;
        }
    }

    @Override
    public void setEquippedStack(EquipmentSlot var1, ItemStack var2) {
        switch (var1) {
            case MAINHAND:
                this.handItems.set(0, var2);
                break;
            case OFFHAND:
                this.handItems.set(1, var2);
                break;
            case FEET:
                this.armorItems.set(0, var2);
                break;
            case LEGS:
                this.armorItems.set(1, var2);
                break;
            case CHEST:
                this.armorItems.set(2, var2);
                break;
            case HEAD:
                this.armorItems.set(3, var2);
                break;
        }
    }

    /*public RagdollEntity(World world, BaseRagdoll ragdoll) {
        this(world);
        this.noClip = true;

        //this(0.15F, 0.15F);

        //remainingLife = 16000;

        this.ragdoll = ragdoll;

        //this.ignoreFrustumCheck = true;
    }*/

    public boolean canBeCollidedWith() {
        return false;
    }

    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(0D);
    }

    @Override
    public void tick() {
        this.prevRenderX = this.x;
        this.prevRenderY = this.y;
        this.prevRenderZ = this.z;
        ++this.age;

        if (this.ragdoll == null) {
            this.destroy();
            return;
        }

        if (remainingLife-- < 0) {

            for (int i = 0; i < 10; ++i) {
                float poofSize = 1.0f;
                double d0 = this.random.nextGaussian() * 0.04D;
                double d1 = this.random.nextGaussian() * 0.04D;
                double d2 = this.random.nextGaussian() * 0.04D;

                this.world.addParticle(ParticleTypes.CLOUD, this.x + (double) (this.random.nextFloat() * poofSize * 2.0F) - (double) poofSize, this.y + this.getHeight() / 2 + (double) (this.random.nextFloat() * this.getHeight()), this.z + (double) (this.random.nextFloat() * poofSize * 2.0F) - (double) poofSize, d0, d1, d2);
            }

            this.remove();
            return;
        }

        this.ragdoll.update(this);

        PointD ragdollPos = this.ragdoll.skeleton.points.get(0).toPoint();

        this.setPosition(this.x + ragdollPos.x, this.y + ragdollPos.y, this.z + ragdollPos.z);

        this.ragdoll.shiftPos(-ragdollPos.x, -ragdollPos.y, -ragdollPos.z);
    }

    @Override
    public Arm getMainArm() {
        return null;
    }

    public void setRemainingLife(int ticks) {
        this.remainingLife = ticks;
    }

    @Override
    public void readCustomDataFromTag(CompoundTag compoundTag) {

    }

    @Override
    public void writeCustomDataToTag(CompoundTag compoundTag) {

    }

    public void setSpawnPosition(double posX, double posY, double posZ) {
        posY += (ragdoll.centerHeightOffset / 16f);
        this.x = posX;
        this.y = posY;
        this.z = posZ;
        float f = this.getWidth() / 2.0F;
        float f1 = this.getHeight();
        this.setBoundingBox(new Box(posX - (double) f, posY, posZ - (double) f, posX + (double) f, posY + (double) f1, posZ + (double) f));
    }

    /**
     * Sets the rotation of the entity
     */
    public void setRotation(float rotYaw/*, float p_70101_2_*/) {
        this.ragdoll.rotateRagdoll(rotYaw);
        //this.rotationPitch = p_70101_2_ % 360.0F;
    }

}
