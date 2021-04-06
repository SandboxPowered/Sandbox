package org.sandboxpowered.loader.mixin.injection.item.tool;

import net.minecraft.world.item.Tier;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.item.tool.ToolMaterial;
import org.sandboxpowered.api.recipe.Ingredient;
import org.spongepowered.asm.mixin.*;

@Mixin(Tier.class)
@Implements(@Interface(iface = ToolMaterial.class, prefix = "material$", remap = Interface.Remap.NONE))
@Unique
public interface MixinTier {
    @Shadow
    int getUses();

    @Shadow
    float getSpeed();

    @Shadow
    float getAttackDamageBonus();

    @Shadow
    int getLevel();

    @Shadow
    int getEnchantmentValue();

    default int material$getDurability() {
        return getUses();
    }

    default float material$getDigSpeed() {
        return getSpeed();
    }

    default float material$getAttackDamage() {
        return getAttackDamageBonus();
    }

    default int material$getMiningLevel() {
        return getLevel();
    }

    default int material$getEnchantmentValue() {
        return getEnchantmentValue();
    }

    default Ingredient<ItemStack> material$getRepairIngredient() {
        return null;
    }
}
