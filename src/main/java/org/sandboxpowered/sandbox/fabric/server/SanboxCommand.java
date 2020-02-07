package org.sandboxpowered.sandbox.fabric.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import org.sandboxpowered.sandbox.api.addon.AddonSpec;

public class SanboxCommand {

    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        commandDispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("sandbox")
                .then(LiteralArgumentBuilder.<ServerCommandSource>literal("addons")
                        .executes(SanboxCommand::executeAddons)
                )
        );
    }

    public static int executeAddons(CommandContext<ServerCommandSource> context) {
        int n = SandboxServer.INSTANCE.loader.getAddons().size();
        if (n == 1) context.getSource().sendFeedback(new LiteralText("There is 1 addon installed:"), false);
        else context.getSource().sendFeedback(new LiteralText("There are " + n + " addons installed:"), false);
        for (AddonSpec a : SandboxServer.INSTANCE.loader.getAddons()) {
            context.getSource().sendFeedback(new LiteralText(a.getTitle() + " [" + a.getModid() + "@" + a.getVersion().toString() + "]"), false);
        }
        return 0;
    }
}
