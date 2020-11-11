package org.sandboxpowered.loader.wrapper;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.util.text.Text;
import org.sandboxpowered.loader.Wrappers;

import java.util.IdentityHashMap;
import java.util.List;

public class WrappedItem extends net.minecraft.world.item.Item {
    private final Item item;

    private static final IdentityHashMap<Item, WrappedItem> ITEM_MAP = new IdentityHashMap<>();

    public static net.minecraft.world.item.Item convertSandboxItem(Item item) {
        if(item instanceof net.minecraft.world.item.Item)
            return (net.minecraft.world.item.Item) item;
        return ITEM_MAP.computeIfAbsent(item, WrappedItem::new);
    }

    public static Item convertVanillaItem(net.minecraft.world.item.Item item) {
        if (item instanceof WrappedItem)
            return ((WrappedItem) item).item;
        return (Item) item;
    }

    public WrappedItem(Item item) {
        super(new Properties());
        this.item = item;
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
}
