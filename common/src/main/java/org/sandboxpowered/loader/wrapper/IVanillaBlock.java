package org.sandboxpowered.loader.wrapper;

import org.sandboxpowered.api.item.tool.ToolType;

public interface IVanillaBlock {
    void sandbox_setHarvestParams(ToolType type, int harvestLevel);
}
