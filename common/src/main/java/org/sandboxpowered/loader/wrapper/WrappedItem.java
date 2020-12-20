package org.sandboxpowered.loader.wrapper;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.item.BlockItem;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.util.text.Text;
import org.sandboxpowered.loader.Wrappers;

import java.util.IdentityHashMap;
import java.util.List;

public class WrappedItem extends net.minecraft.world.item.Item implements IWrappedItem {
    private static final IdentityHashMap<Item, IWrappedItem> ITEM_MAP = new IdentityHashMap<>();
    private final Item item;

    public WrappedItem(Item item) {
        super(new Properties());
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