package io.github.jfglzs.asa.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.jfglzs.asa.utils.*;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.concurrent.CompletableFuture;

public class ServerManagerCommand {
    //TODO 指令/快捷键切换服务器
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        var command = CommandUtils.literal("serverManager")
                .requires(cs -> false)
                .then(CommandUtils.literal("list"));
        dispatcher.register(command);
    }

    private static CompletableFuture<Suggestions> getPrefixSuggestions(CommandContext<FabricClientCommandSource> sp, SuggestionsBuilder c) {
        return c.buildFuture();
    }
}

