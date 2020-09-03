package org.sandboxpowered.sandbox.fabric.mixin.editor;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.WorldBorderCommand;
import org.sandboxpowered.sandbox.fabric.editor.EditorCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommandManager.class)
public class MixinCommandManager {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/WorldBorderCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V"))
    public void constructor(CommandDispatcher<ServerCommandSource> dispatcher) {
        WorldBorderCommand.register(dispatcher);
        EditorCommand.register(dispatcher);
    }
}
