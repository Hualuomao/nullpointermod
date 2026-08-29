package com.example.nullpointermod.command;

import com.example.nullpointermod.NullPointerMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class NullPointerCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("nullpointer")
                .requires(source -> source.hasPermission(2)) // 需要 OP 权限
                .then(Commands.literal("damage")
                        .then(Commands.argument("mode", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    builder.suggest("enable");
                                    builder.suggest("disable");
                                    return builder.buildFuture();
                                })
                                .executes(NullPointerCommand::executeDamage)
                        )
                )
        );
    }

    private static int executeDamage(CommandContext<CommandSourceStack> context) {
        String mode = StringArgumentType.getString(context, "mode");
        boolean enable = mode.equalsIgnoreCase("enable");

        NullPointerMod.ENABLE_DAMAGE = enable;
        String status = enable ? "§a开启" : "§c关闭";

        context.getSource().sendSuccess(
                Component.literal("§6[NullPointerMod] §r实体伤害已" + status),
                true
        );
        NullPointerMod.LOGGER.info("实体伤害已{}（由 {} 执行）",
                enable ? "开启" : "关闭",
                context.getSource().getTextName());

        return 1;
    }
}
