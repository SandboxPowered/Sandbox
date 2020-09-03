package org.sandboxpowered.sandbox.fabric.editor.injection;

import org.sandboxpowered.api.editor.Area;

import java.util.List;

public interface EditorWorld {
    List<Area> editor_getAreas();
    void editor_addArea(Area area);
    void editor_removeArea(Area area);
}
