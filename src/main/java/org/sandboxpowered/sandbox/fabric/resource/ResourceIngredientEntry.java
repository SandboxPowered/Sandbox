package org.sandboxpowered.sandbox.fabric.resource;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.sandboxpowered.api.content.Content;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.resources.Resource;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

import java.util.Collection;
import java.util.Collections;

public class ResourceIngredientEntry<C extends Content<C>> implements Ingredient.Entry {
    private final Resource<C> resource;

    public ResourceIngredientEntry(Resource<C> resource) {
        this.resource = resource;
    }

    @Override
    public Collection<ItemStack> getStacks() {
        return Collections.singleton(WrappingUtil.convert(resource.getType().createItemStack(resource.get())));
    }

    @Override
    public JsonObject toJson() {
        JsonObject tag = new JsonObject();
        JsonObject type = new JsonObject();
        type.addProperty("material", resource.getMaterial().toString());
        type.addProperty("type", resource.getType().toString());
        tag.add("resource", type);
        return tag;
    }
}