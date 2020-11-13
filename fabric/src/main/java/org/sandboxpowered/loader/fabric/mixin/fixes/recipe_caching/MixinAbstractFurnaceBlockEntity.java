package org.sandboxpowered.loader.fabric.mixin.fixes.recipe_caching;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class MixinAbstractFurnaceBlockEntity extends BaseContainerBlockEntity {
    @Shadow
    public abstract ItemStack getItem(int i);

    public MixinAbstractFurnaceBlockEntity(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
    }

    private AbstractCookingRecipe cachedRecipe = null;
    private ItemStack noRecipeStack = null;

    /**
     * @reason Improves speed of furnace by caching the recipe until the input becomes invalid for the recipe
     */
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"))
    private <C extends Container> Optional<AbstractCookingRecipe> getCachedRecipe(RecipeManager manager, RecipeType<AbstractCookingRecipe> recipeType, C container, Level level) {
        ItemStack input = getItem(0);
        if (input.isEmpty() || input == noRecipeStack) {
            return Optional.empty();
        }

        if (cachedRecipe != null && cachedRecipe.matches(container, level)) {
            return Optional.of(cachedRecipe);
        } else {
            AbstractCookingRecipe rec = manager.getRecipeFor(recipeType, container, level).orElse(null);
            if (rec == null) noRecipeStack = input;
            else noRecipeStack = null;
            cachedRecipe = rec;
            return Optional.ofNullable(rec);
        }
    }
}