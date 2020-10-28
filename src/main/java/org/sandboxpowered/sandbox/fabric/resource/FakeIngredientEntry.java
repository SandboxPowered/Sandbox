package org.sandboxpowered.sandbox.fabric.resource;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import java.util.Collection;
import java.util.Collections;

public class FakeIngredientEntry implements Ingredient.Entry {
    public static final FakeIngredientEntry ENTRY = new FakeIngredientEntry();
    @Override
    public Collection<ItemStack> getStacks() {
        return Collections.emptySet();
    }

    @Override
    public JsonObject toJson() {
        return new JsonObject();
    }
}
