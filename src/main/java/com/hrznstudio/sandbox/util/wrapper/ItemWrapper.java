package com.hrznstudio.sandbox.util.wrapper;

import com.hrznstudio.sandbox.api.item.IItem;
import com.hrznstudio.sandbox.api.item.ItemStack;
import com.hrznstudio.sandbox.api.util.InteractionResult;
import com.hrznstudio.sandbox.api.util.math.Position;
import com.hrznstudio.sandbox.api.world.World;
import com.hrznstudio.sandbox.util.WrappingUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

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
}
