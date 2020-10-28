package org.sandboxpowered.sandbox.fabric.mixin.fabric.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.recipe.Ingredient;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.resources.Resource;
import org.sandboxpowered.api.resources.ResourceMaterial;
import org.sandboxpowered.api.resources.ResourceType;
import org.sandboxpowered.sandbox.fabric.loader.SandboxLoader;
import org.sandboxpowered.sandbox.fabric.resource.FakeIngredientEntry;
import org.sandboxpowered.sandbox.fabric.resource.GlobalResourceRegistrationService;
import org.sandboxpowered.sandbox.fabric.resource.ResourceIngredientEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Ingredient.class)
public class MixinIngredient {
    @Inject(method = "entryFromJson", at = @At("HEAD"), cancellable = true)
    private static void entryFromJson(JsonObject object, CallbackInfoReturnable<Ingredient.Entry> info) {
        boolean hasResource = object.has("resource");
        boolean hasItem = object.has("item");
        boolean hasTag = object.has("tag");
        if (hasResource ? (hasItem || hasTag) : (hasItem && hasTag)) {
            throw new JsonParseException("An ingredient entry is either a resource, a tag or an item, not all");
        }
        if (hasResource) {
            if (SandboxLoader.loader != null && SandboxLoader.loader.getFabric() != null) {
                JsonObject obj = object.getAsJsonObject("resource");
                if (!obj.has("material") || !obj.has("type"))
                    throw new JsonParseException("A resource ingredient needs a material and type");
                String materialStr = obj.get("material").getAsString();
                String typeStr = obj.get("type").getAsString();
                ResourceMaterial material = ResourceMaterial.of(materialStr);
                ResourceType<?> type = ResourceType.find(typeStr);
                if (type == null)
                    info.setReturnValue(FakeIngredientEntry.ENTRY);
                GlobalResourceRegistrationService resourceService = SandboxLoader.loader.getFabric().getResourceRegistration();
                Resource<?> resource = resourceService.getResource(material, type);
                if (resource == null)
                    info.setReturnValue(FakeIngredientEntry.ENTRY);
                info.setReturnValue(new ResourceIngredientEntry<>(resource));
            } else {
                info.setReturnValue(FakeIngredientEntry.ENTRY);
            }
        }
    }
}