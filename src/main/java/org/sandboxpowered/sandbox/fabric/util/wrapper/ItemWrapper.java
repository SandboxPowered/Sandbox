package org.sandboxpowered.sandbox.fabric.util.wrapper;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.sandboxpowered.api.item.BlockItem;
import org.sandboxpowered.api.item.BucketItem;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.util.InteractionResult;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

import org.jetbrains.annotations.Nullable;
import java.util.LinkedList;
import java.util.List;

public class ItemWrapper extends net.minecraft.item.Item implements SandboxInternal.ItemWrapper {
    private final Item iItem;

    public ItemWrapper(Item iItem) {
        super(WrappingUtil.convert(iItem.getSettings()));
        this.iItem = iItem;
    }

    public static SandboxInternal.ItemWrapper create(Item iItem) {
        if (iItem instanceof BucketItem)
            return new BucketItemWrapper((BucketItem) iItem);
        if (iItem instanceof BlockItem)
            return new BlockItemWrapper((BlockItem) iItem);
        return new ItemWrapper(iItem);
    }

    @Override
    public Item getItem() {
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
        List<org.sandboxpowered.api.util.text.Text> tooltip = new LinkedList<>();
        iItem.appendTooltipText(
                WrappingUtil.cast(itemStack_1, ItemStack.class),
                world_1 == null ? null : (World) world_1,
                tooltip,
                tooltipContext_1.isAdvanced()
        );
        tooltip.forEach(text -> list_1.add(WrappingUtil.convert(text)));
        super.appendTooltip(itemStack_1, world_1, list_1, tooltipContext_1);
    }

    public static class BlockItemWrapper extends net.minecraft.item.BlockItem implements SandboxInternal.ItemWrapper {
        private final BlockItem item;

        public BlockItemWrapper(BlockItem item) {
            super(WrappingUtil.convert(item.asBlock()), WrappingUtil.convert(item.getSettings()));
            this.item = item;
        }

        public BlockItem getIBlockItem() {
            return item;
        }

        @Override
        public Item getItem() {
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
            List<org.sandboxpowered.api.util.text.Text> tooltip = new LinkedList<>();
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

    public static class BucketItemWrapper extends net.minecraft.item.BucketItem implements SandboxInternal.ItemWrapper {
        private final BucketItem item;

        public BucketItemWrapper(BucketItem item) {
            super(WrappingUtil.convert(item.getFluid()), WrappingUtil.convert(item.getSettings()));
            this.item = item;
        }

        @Override
        public Item getItem() {
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
            List<org.sandboxpowered.api.util.text.Text> tooltip = new LinkedList<>();
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
