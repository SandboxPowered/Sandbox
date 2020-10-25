package org.sandboxpowered.sandbox.fabric.util.wrapper;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;
import org.sandboxpowered.api.item.BlockItem;
import org.sandboxpowered.api.item.BucketItem;
import org.sandboxpowered.api.item.Item;
import org.sandboxpowered.api.item.ItemStack;
import org.sandboxpowered.api.util.InteractionResult;
import org.sandboxpowered.api.util.math.Position;
import org.sandboxpowered.api.world.World;
import org.sandboxpowered.sandbox.fabric.internal.SandboxInternal;
import org.sandboxpowered.sandbox.fabric.util.WrappingUtil;

import java.util.LinkedList;
import java.util.List;

public class ItemWrapper extends net.minecraft.item.Item implements SandboxInternal.IItemWrapper {
    private final Item item;

    public ItemWrapper(Item item) {
        super(WrappingUtil.convert(item.getSettings()));
        this.item = item;
    }

    public static SandboxInternal.IItemWrapper create(Item iItem) {
        if (iItem instanceof BucketItem)
            return new BucketItemWrapper((BucketItem) iItem);
        if (iItem instanceof BlockItem)
            return new BlockItemWrapper((BlockItem) iItem);
        return new ItemWrapper(iItem);
    }

    @Override
    public Item getSandboxItem() {
        return item;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        InteractionResult result = item.onItemUsed(
                (World) context.getWorld(),
                (Position) context.getBlockPos(),
                WrappingUtil.cast(context.getStack(), ItemStack.class)
        );
        return WrappingUtil.convert(result);
    }

    @Override
    public float getMiningSpeedMultiplier(net.minecraft.item.ItemStack itemStack, BlockState blockState) {
        return item.getMiningSpeed(WrappingUtil.convert(itemStack), WrappingUtil.convert(blockState));
    }

    @Override
    public void appendTooltip(net.minecraft.item.ItemStack stack, @Nullable net.minecraft.world.World world, List<Text> list, TooltipContext tooltipContext) {
        List<org.sandboxpowered.api.util.text.Text> tooltip = new LinkedList<>();
        item.appendTooltipText(
                WrappingUtil.cast(stack, ItemStack.class),
                world == null ? null : (World) world,
                tooltip,
                tooltipContext.isAdvanced()
        );
        tooltip.forEach(text -> list.add(WrappingUtil.convert(text)));
        super.appendTooltip(stack, world, list, tooltipContext);
    }

    public static class BlockItemWrapper extends net.minecraft.item.BlockItem implements SandboxInternal.IItemWrapper {
        private final BlockItem item;

        public BlockItemWrapper(BlockItem item) {
            super(WrappingUtil.convert(item.asBlock()), WrappingUtil.convert(item.getSettings()));
            this.item = item;
        }

        @Override
        public Item getSandboxItem() {
            return item;
        }

        @Override
        public ActionResult useOnBlock(ItemUsageContext context) {
            InteractionResult result = item.onItemUsed(
                    (World) context.getWorld(),
                    (Position) context.getBlockPos(),
                    WrappingUtil.cast(context.getStack(), ItemStack.class)
            );
            if (result == InteractionResult.IGNORE)
                return super.useOnBlock(context);
            return WrappingUtil.convert(result);
        }

        @Override
        public float getMiningSpeedMultiplier(net.minecraft.item.ItemStack itemStack, BlockState blockState) {
            return item.getMiningSpeed(WrappingUtil.convert(itemStack), WrappingUtil.convert(blockState));
        }

        @Override
        public void appendTooltip(net.minecraft.item.ItemStack stack, @Nullable net.minecraft.world.World world, List<Text> list, TooltipContext tooltipContext) {
            List<org.sandboxpowered.api.util.text.Text> tooltip = new LinkedList<>();
            item.appendTooltipText(
                    WrappingUtil.cast(stack, ItemStack.class),
                    world == null ? null : (World) world,
                    tooltip,
                    tooltipContext.isAdvanced()
            );
            tooltip.forEach(text -> list.add(WrappingUtil.convert(text)));
            super.appendTooltip(stack, world, list, tooltipContext);
        }
    }

    public static class BucketItemWrapper extends net.minecraft.item.BucketItem implements SandboxInternal.IItemWrapper {
        private final BucketItem item;

        public BucketItemWrapper(BucketItem item) {
            super(WrappingUtil.convert(item.getFluid()), WrappingUtil.convert(item.getSettings()));
            this.item = item;
        }

        @Override
        public float getMiningSpeedMultiplier(net.minecraft.item.ItemStack itemStack, BlockState blockState) {
            return item.getMiningSpeed(WrappingUtil.convert(itemStack), WrappingUtil.convert(blockState));
        }

        @Override
        public Item getSandboxItem() {
            return item;
        }

        @Override
        public ActionResult useOnBlock(ItemUsageContext context) {
            InteractionResult result = item.onItemUsed(
                    (World) context.getWorld(),
                    (Position) context.getBlockPos(),
                    WrappingUtil.cast(context.getStack(), ItemStack.class)
            );
            if (result == InteractionResult.IGNORE)
                return super.useOnBlock(context);
            return WrappingUtil.convert(result);
        }

        @Override
        public void appendTooltip(net.minecraft.item.ItemStack stack, @Nullable net.minecraft.world.World world, List<Text> list, TooltipContext tooltipContext) {
            List<org.sandboxpowered.api.util.text.Text> tooltip = new LinkedList<>();
            item.appendTooltipText(
                    WrappingUtil.cast(stack, ItemStack.class),
                    world == null ? null : (World) world,
                    tooltip,
                    tooltipContext.isAdvanced()
            );
            tooltip.forEach(text -> list.add(WrappingUtil.convert(text)));
            super.appendTooltip(stack, world, list, tooltipContext);
        }
    }
}
