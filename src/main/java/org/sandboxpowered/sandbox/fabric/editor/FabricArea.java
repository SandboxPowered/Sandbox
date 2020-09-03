package org.sandboxpowered.sandbox.fabric.editor;

import org.sandboxpowered.api.editor.Area;
import org.sandboxpowered.api.shape.Box;

public class FabricArea implements Area {
    private Box box;

    @Override
    public Box getBox() {
        return box;
    }
}
