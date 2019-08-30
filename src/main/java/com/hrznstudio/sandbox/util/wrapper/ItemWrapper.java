package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.api.SandboxInternal;
import com.hrznstudio.sandbox.api.item.IBlockItem;
import com.hrznstudio.sandbox.api.item.IItem;
import com.hrznstudio.sandbox.api.item.ItemStack;
import com.hrznstudio.sandbox.api.util.InteractionResult;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.World;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class ItemWrapper extends Item implements SandboxInternal.ItemWrapper {
    private IItem iItem;

    public ItemWrapper(IItem iItem) {
        super(new Settings());
        this.iItem = iItem;
    }

    public static Item create(IItem iItem) {
        if (iItem instanceof IBlockItem)
            return new BlockItemWrapper((IBlockItem) iItem);
        return new ItemWrapper(iItem);
    }

    @Override
    public IItem getItem() {
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
        super.appendTooltip(itemStack_1, world_1, list_1, tooltipContext_1);
    }

    public static class BlockItemWrapper extends BlockItem implements SandboxInternal.ItemWrapper {
        private IBlockItem item;

        public BlockItemWrapper(IBlockItem item) {
            super(WrappingUtil.convert(item.asBlock()), new Settings());
            this.item = item;
        }

        public IBlockItem getIBlockItem() {
            return item;
        }

        @Override
        public IItem getItem() {
            return item;
        }

        @Override
        public ActionResult useOnBlock(ItemUsageContext itemUsageContext_1) {
            InteractionResult result = item.onItemUsed(
                    (World) itemUsageContext_1.getWorld(),
                    (Position) itemUsageContext_1.getBlockPos(),
                    WrappingUtil.cast(itemUsageContext_1.getStack(), ItemStack.class)
            );
            if (result == InteractionResult.IGNORE)
                return super.useOnBlock(itemUsageContext_1);
            return result == InteractionResult.SUCCESS ? ActionResult.SUCCESS : result == InteractionResult.FAILURE ? ActionResult.FAIL : ActionResult.PASS;
        }

        @Override
        public void appendTooltip(net.minecraft.item.ItemStack itemStack_1, @Nullable net.minecraft.world.World world_1, List<Text> list_1, TooltipContext tooltipContext_1) {
            List<com.hrznstudio.sandbox.api.util.text.Text> tooltip = new LinkedList<>();
            item.appendTooltipText(
                    WrappingUtil.cast(itemStack_1, ItemStack.class),
                    world_1 == null ? null : (World) world_1,
                    tooltip,
                    tooltipContext_1.isAdvanced()
            );
            tooltip.forEach(text -> list_1.add(WrappingUtil.convert(text)));
            super.appendTooltip(itemStack_1, world_1, list_1, tooltipContext_1);
        }
    }
}
