package io.github.jfglzs.asa.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.feature.autoVault.AutoVaultExecutor;
import io.github.jfglzs.asa.utils.*;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.core.BlockPos;

public class AutoVaultCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        var command = CommandUtils.literal("autovault")
                .requires(cs -> Configs.AUTO_VAULT_COMMAND.getBooleanValue())
                .then(CommandUtils.literal("set")
                        .then(CommandUtils.argument("blockX", IntegerArgumentType.integer()))
                        .then(CommandUtils.argument("blockY", IntegerArgumentType.integer()))
                        .then(CommandUtils.argument("blockZ", IntegerArgumentType.integer()))
                        .executes(AutoVaultCommand::setPos)
                )
                .then(CommandUtils.literal("player")
                        .then(CommandUtils.argument("prefix", StringArgumentType.word()))
                        .then(CommandUtils.argument("start", IntegerArgumentType.integer(0)))
                        .then(CommandUtils.argument("end", IntegerArgumentType.integer(0)))
                        .then(CommandUtils.argument("blockX", FloatArgumentType.floatArg()))
                        .then(CommandUtils.argument("blockY", FloatArgumentType.floatArg()))
                        .then(CommandUtils.argument("blockZ", FloatArgumentType.floatArg()))
                        .then(CommandUtils.argument("direction", FloatArgumentType.floatArg()))
                        .then(CommandUtils.argument("in", FloatArgumentType.floatArg()))
                        .executes(AutoVaultCommand::setPlayer)
                )
                .then(CommandUtils.literal("start")
                        .executes(AutoVaultCommand::start)
                )
                .then(CommandUtils.literal("stop")
                        .executes(AutoVaultCommand::stop)
                );
        dispatcher.register(command);
    }

    private static int setPlayer(CommandContext<FabricClientCommandSource> context) {
        String prefix = StringArgumentType.getString(context, "prefix");
        int start = IntegerArgumentType.getInteger(context, "start");
        int end = IntegerArgumentType.getInteger(context, "end");
        float blockX = FloatArgumentType.getFloat(context, "blockX");
        float blockY = FloatArgumentType.getFloat(context, "blockY");
        float blockZ = FloatArgumentType.getFloat(context, "blockZ");
        float direction = FloatArgumentType.getFloat(context, "direction");
        float in = FloatArgumentType.getFloat(context, "in");

        if (start > end) {
            context.getSource().sendError(ChatUtils.toComponent("无效的开始值"));
            return 0;
        }

        AutoVaultExecutor.set(prefix, start, end, blockX, blockY, blockZ, direction, in);
        return Command.SINGLE_SUCCESS;
    }

    private static int setPos(CommandContext<FabricClientCommandSource> context) {
        int x = IntegerArgumentType.getInteger(context, "blockX");
        int y = IntegerArgumentType.getInteger(context, "blockY");
        int z = IntegerArgumentType.getInteger(context, "blockZ");
        if (!AutoVaultExecutor.setBlockPos(new BlockPos(x, y, z))) {
            context.getSource().sendError(ChatUtils.toComponent("无效的方块坐标"));
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int stop(CommandContext<FabricClientCommandSource> context) {
        AutoVaultExecutor.isRunning = false;
        AutoVaultExecutor.reset();
        return Command.SINGLE_SUCCESS;
    }

    private static int start(CommandContext<FabricClientCommandSource> context) {
        AutoVaultExecutor.isRunning = true;
        return Command.SINGLE_SUCCESS;
    }
}

