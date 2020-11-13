package org.sandboxpowered.loader.forge.mixin.fixes.recipe_caching;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(AbstractFurnaceTileEntity.class)
public abstract class MixinAbstractFurnaceTileEntity extends LockableTileEntity {
    @Shadow
    public abstract ItemStack getItem(int i);

    public MixinAbstractFurnaceTileEntity(TileEntityType<?> p_i48285_1_) {
        super(p_i48285_1_);
    }

    private AbstractCookingRecipe cachedRecipe = null;
    private ItemStack noRecipeStack = null;

    /**
     * @reason Improves speed of furnace by caching the recipe until the input becomes invalid for the recipe
     */
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/crafting/RecipeManager;getRecipeFor(Lnet/minecraft/item/crafting/IRecipeType;Lnet/minecraft/inventory/IInventory;Lnet/minecraft/world/World;)Ljava/util/Optional;"))
    private <C extends IInventory> Optional<AbstractCookingRecipe> getCachedRecipe(RecipeManager manager, IRecipeType<AbstractCookingRecipe> recipeType, C container, World world) {
        ItemStack input = getItem(0);
        if (input.isEmpty() || input == noRecipeStack) {
            return Optional.empty();
        }

        if (cachedRecipe != null && cachedRecipe.matches(container, world)) {
            return Optional.of(cachedRecipe);
        } else {
            AbstractCookingRecipe rec = manager.getRecipeFor(recipeType, container, world).orElse(null);
            if (rec == null) noRecipeStack = input;
            else noRecipeStack = null;
            cachedRecipe = rec;
            return Optional.ofNullable(rec);
        }
    }
}