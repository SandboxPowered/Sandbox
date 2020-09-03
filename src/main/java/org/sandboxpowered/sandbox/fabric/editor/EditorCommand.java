package org.sandboxpowered.sandbox.fabric.editor;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class EditorCommand {

    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        commandDispatcher.register(
                CommandManager.literal("editor")
                        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                        .then(CommandManager.literal("area")
                                .then(CommandManager.literal("add")
                                        .then(CommandManager.argument("start", Vec3ArgumentType.vec3(false))
                                                .then(CommandManager.argument("end", Vec3ArgumentType.vec3(false)))).executes(context -> {
                                            context.getSource().sendFeedback(Text.of("hi"), false);
                                            World world = context.getSource().getWorld();
                                            return 1;
                                        }))
                        )
        );
    }
}