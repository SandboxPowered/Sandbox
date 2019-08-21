package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.api.item.IItem;
import com.hrznstudio.sandbox.api.item.ItemStack;
import com.hrznstudio.sandbox.api.util.InteractionResult;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.World;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class ItemWrapper extends Item {
    private IItem iItem;

    public ItemWrapper(IItem iItem) {
        super(new Settings());
        this.iItem = iItem;
    }

    public static ItemWrapper create(IItem iItem) {
        return new ItemWrapper(iItem);
    }

    public IItem getiItem() {
        return iItem;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext itemUsageContext_1) {
        InteractionResult result = iItem.onItemUsed(
                (World) itemUsageContext_1.getWorld(),
                (Position) itemUsageContext_1.getBlockPos(),
                WrappingUtil.cast(itemUsageContext_1.getStack(), ItemStack.class)
        );
        return result == InteractionResult.SUCCESS ? ActionResult.SUCCESS : result == InteractionResult.FAILURE ? ActionResult.FAIL : ActionResult.PASS;
    }

    @Override
    public void appendTooltip(net.minecraft.item.ItemStack itemStack_1, @Nullable net.minecraft.world.World world_1, List<Text> list_1, TooltipContext tooltipContext_1) {
        List<com.hrznstudio.sandbox.api.util.text.Text> tooltip = new LinkedList<>();
        iItem.appendTooltipText(
                WrappingUtil.cast(itemStack_1, ItemStack.class),
                world_1 == null ? null : (World) world_1,
                tooltip,
                tooltipContext_1.isAdvanced()
        );
        tooltip.forEach(text -> list_1.add(WrappingUtil.convert(text)));
    }
}
