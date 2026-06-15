package io.github.jfglzs.asa.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.*;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class PlayerManipulateCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        var command = CommandUtils.literal("pm")
                .requires(source -> Configs.PLAYER_MANIPULATE_COMMAND.getBooleanValue())
                .then(CommandUtils.argument("prefix", StringArgumentType.word()).then(makeCommand(true)))
                .then(makeCommand(false));
        dispatcher.register(command);
    }

    private static ArgumentBuilder<FabricClientCommandSource, ?> makeCommand(boolean enablePrefix) {
        return CommandUtils.argument("start", IntegerArgumentType.integer(1))
                .then(CommandUtils.argument("end", IntegerArgumentType.integer(1, 1000))
                        .then(CommandUtils.argument("action", StringArgumentType.greedyString())
                                .executes(source -> process(source, enablePrefix)))
                );
    }

    private static int process(CommandContext<FabricClientCommandSource> source, boolean enablePrefix) {
        var prefix = enablePrefix ? StringArgumentType.getString(source, "prefix") : Configs.PLAYER_MANIPULATE_COMMAND_DEFAULT_PREFIX.getStringValue();
        var start = IntegerArgumentType.getInteger(source, "start");
        var end = IntegerArgumentType.getInteger(source, "end");
        var action = StringArgumentType.getString(source, "action");
        ThreadUtils.runAsync(() -> startWithAction(prefix, start, end, action));
        return Command.SINGLE_SUCCESS;
    }

    private static void startWithAction(String prefix, int start, int end, String action) {
        for (int i = start; i <= end; i++) {
            try {
                Thread.sleep(Configs.PLAYER_MANIPULATE_COMMAND_WAIT_TIME.getIntegerValue());
                var playerName = prefix == null ? i : prefix + i;
                ChatUtils.sendOverLayMessage(ProgressBar.getProgress((double) i / (end - start)));
                ThreadUtils.runOnClientThread(() -> MCUtils.executeCommand("player %s %s".formatted(playerName, action)));
            }
            catch (Exception e) {
                AsaMod.LOGGER.error("cant execute player action: {}", action, e);
                return;
            }
        }
    }
}
