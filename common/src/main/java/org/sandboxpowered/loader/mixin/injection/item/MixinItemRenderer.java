package org.sandboxpowered.loader.mixin.injection.item;

import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.sandboxpowered.api.events.ItemEvents;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.loader.Wrappers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    private final ThreadLocal<org.sandboxpowered.api.item.ItemStack> stackThreadLocal = new ThreadLocal<>();

    @Redirect(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isDamaged()Z"))
    public boolean shouldRenderBar(ItemStack itemStack) {
        org.sandboxpowered.api.item.ItemStack sbxStack = stackThreadLocal.get();
        Item item = sbxStack.getItem();
        if (ItemEvents.SHOW_DURABILITY_BAR.hasSubscribers())
            return ItemEvents.SHOW_DURABILITY_BAR.post((event, show) -> event.onEvent(sbxStack, show), item.showDurabilityBar(sbxStack));
        return item.showDurabilityBar(sbxStack);
    }

    @Redirect(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getMaxDamage()I"))
    public int maxDamage(ItemStack itemStack) {
        if (itemStack.getMaxDamage() == 0)
            return 1;
        return itemStack.getMaxDamage();
    }

    @Redirect(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getDamageValue()I"))
    public int damageValue(ItemStack itemStack) {
        if (itemStack.getMaxDamage() == 0)
            return 0;
        return itemStack.getDamageValue();
    }

    @Redirect(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Ljava/lang/Math;round(F)I"))
    public int damageFloat(float v) {
        org.sandboxpowered.api.item.ItemStack stack = stackThreadLocal.get();
        if (ItemEvents.GET_DURABILITY_VALUE.hasSubscribers()) {
            float value = stack.getItem().getDurabilityBarValue(stack);
            return Math.round(ItemEvents.GET_DURABILITY_VALUE.post((event, current) -> event.onEvent(stack, current), value) * 13f);
        }
        return Math.round(stack.getItem().getDurabilityBarValue(stack) * 13f);
    }

    @Redirect(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;hsvToRgb(FFF)I"))
    public int damageColour(float hue, float saturation, float brightness) {
        org.sandboxpowered.api.item.ItemStack stack = stackThreadLocal.get();
        if (ItemEvents.GET_DURABILITY_COLOR.hasSubscribers()) {
            return ItemEvents.GET_DURABILITY_COLOR.post((event, current) -> event.onEvent(stack, current), stack.getItem().getDurabilityBarColor(stack));
        }
        return stack.getItem().getDurabilityBarColor(stack);
    }

    @Redirect(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getCount()I", ordinal = 0))
    public int renderStackSize(ItemStack itemStack) {
        org.sandboxpowered.api.item.ItemStack sandboxStack = Wrappers.ITEMSTACK.toSandbox(itemStack);
        stackThreadLocal.set(sandboxStack);
        if (ItemEvents.DRAW_STACK_COUNT.hasSubscribers()) {
            return ItemEvents.DRAW_STACK_COUNT.post((event, current) -> event.onEvent(sandboxStack, current), sandboxStack.getItem().shouldRenderStackCount(sandboxStack)) ? itemStack.getCount() : 1;
        }
        return itemStack.getCount();
    }
}