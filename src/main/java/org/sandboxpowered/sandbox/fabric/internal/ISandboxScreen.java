package org.sandboxpowered.sandbox.fabric.internal;

import net.minecraft.client.gui.widget.AbstractButtonWidget;

import java.util.List;

public interface ISandboxScreen {
    List<AbstractButtonWidget> getButtons();
}
