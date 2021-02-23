package org.sandboxpowered.loader.wrapper;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.item.BlockItem;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.util.text.Text;
import org.sandboxpowered.loader.Wrappers;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class WrappedItem extends net.minecraft.world.item.Item implements IWrappedItem {
    private static final IdentityHashMap<Item, IWrappedItem> ITEM_MAP = new IdentityHashMap<>();
    private final Item item;

    public static Properties convert(Item.Settings settings) {
        Properties properties = new Properties();
        properties.stacksTo(settings.getStackSize());
        properties.durability(settings.getDurability());
        if (settings.getRecipeRemainder() != null)
            properties.craftRemainder(Wrappers.ITEM.toVanilla(settings.getRecipeRemainder()));
        return properties;
    }

    public WrappedItem(Item item) {
        super(convert(item.getSettings()));
        this.item = item;
    }

    @Override
    public Item getAsSandbox() {
        return this.item;
    }

    @Override
    public net.minecraft.world.item.Item getAsVanilla() {
        return this;
    }

    public static net.minecraft.world.item.Item convertSandboxItem(Item item) {
        if (item instanceof net.minecraft.world.item.Item)
            return (net.minecraft.world.item.Item) item;
        if (item instanceof BlockItem)
            return ITEM_MAP.computeIfAbsent(item, i -> new WrappedItemBlock((BlockItem) i)).getAsVanilla();
        return ITEM_MAP.computeIfAbsent(item, WrappedItem::new).getAsVanilla();
    }

    public static Item convertVanillaItem(net.minecraft.world.item.Item item) {
        if (item instanceof IWrappedItem)
            return ((IWrappedItem) item).getAsSandbox();
        return (Item) item;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        item.appendTooltipText(
                Wrappers.ITEMSTACK.toSandbox(itemStack),
                Wrappers.WORLD.toSandbox(level),
                (List<Text>) Wrappers.TEXT.toSandboxList(list),
                tooltipFlag.isAdvanced()
        );
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return item.showEnchantmentGlint(Wrappers.ITEMSTACK.toSandbox(itemStack));
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        return item.getMiningSpeed(
                Wrappers.ITEMSTACK.toSandbox(itemStack), Wrappers.BLOCKSTATE.toSandbox(blockState)
        );
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockState) {
        return item.isCorrectToolForDrops(
                Wrappers.BLOCKSTATE.toSandbox(blockState)
        );
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        return item.onMine(
                Wrappers.ITEMSTACK.toSandbox(itemStack),
                Wrappers.WORLD.toSandbox(level),
                Wrappers.POSITION.toSandbox(blockPos),
                Wrappers.BLOCKSTATE.toSandbox(blockState),
                livingEntity.getId()
        );
    }

    @Override
    public int getEnchantmentValue() {
        return item.getEnchantmentValue();
    }

    private Map<EquipmentSlot, Multimap<Attribute, AttributeModifier>> attributeModifiers;

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (attributeModifiers != null && attributeModifiers.containsKey(equipmentSlot))
            return attributeModifiers.get(equipmentSlot);
        Multimap<org.sandboxpowered.api.item.attribute.Attribute, org.sandboxpowered.api.item.attribute.Attribute.Modifier> sbxMap = item.getAttributeModifiers(
                Wrappers.EQUIPMENT_SLOT.toSandbox(equipmentSlot)
        );
        if(sbxMap.isEmpty())
            return super.getDefaultAttributeModifiers(equipmentSlot);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> vanillaBuilder = ImmutableMultimap.builder();
        if (attributeModifiers == null)
            attributeModifiers = new HashMap<>();
        sbxMap.forEach((attribute, modifier) -> vanillaBuilder.put(
                Wrappers.ATTRIBUTE.toVanilla(attribute),
                Wrappers.ATTRIBUTE_MODIFIER.toVanilla(modifier)
        ));
        Multimap<Attribute, AttributeModifier> vanillaMap = vanillaBuilder.build();
        attributeModifiers.put(equipmentSlot, vanillaMap);
        return vanillaMap;
    }

    public static class WrappedItemBlock extends net.minecraft.world.item.BlockItem implements IWrappedItem {
        private final BlockItem item;

        public WrappedItemBlock(BlockItem item) {
            super(Wrappers.BLOCK.toVanilla(item.asBlock()), new Properties());
            this.item = item;
        }

        @Override
        public Item getAsSandbox() {
            return item;
        }

        @Override
        public net.minecraft.world.item.Item getAsVanilla() {
            return this;
        }
    }

}