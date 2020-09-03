package org.sandboxpowered.sandbox.fabric.mixin.editor;

import net.minecraft.world.World;
import org.sandboxpowered.api.editor.Area;
import org.sandboxpowered.sandbox.fabric.editor.injection.EditorWorld;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(World.class)
public class MixinWorld implements EditorWorld {
    private final List<Area> editor_areas = new ArrayList<>();

    @Override
    public List<Area> editor_getAreas() {
        return editor_areas;
    }

    @Override
    public void editor_addArea(Area area) {
        editor_areas.add(area);
    }

    @Override
    public void editor_removeArea(Area area) {
        editor_areas.remove(area);
    }
}
