package io.github.jfglzs.asa.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.CommandUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ServerCommand {
    public static ServerAddress currentAddress = null;

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        var command = CommandUtils.literal("server")
                                  .requires(source -> Configs.Functions.ENABLE_SERVER_COMMAND_MAPPING.getBooleanValue())
                                  .then(CommandUtils.argument("server", StringArgumentType.string())
                                                    .suggests(ServerCommand::suggest)
                                                    .executes(ServerCommand::trySwitch));
        dispatcher.register(command);
    }

    private static int trySwitch(CommandContext<FabricClientCommandSource> context) {
        List<ServerInfo> parsed = tryParse();
        String input = StringArgumentType.getString(context, "server");

        for (ServerInfo info : parsed) {
            String server = info.server();
            ServerAddress ipAddr = info.ipAddr();

            if (server.equals(input)) {
                MCUtils.disconnect();
                MCUtils.connectToServer(server, ipAddr);
                MCUtils.setScreen(null);
                return 0;
            }
            else {
                String proxyServer = server + "-" + info.proxyServer();
                boolean bl = info.proxyServer() != null && proxyServer.equals(input);
                if (bl) {
                    if (ipAddr.equals(currentAddress))
                        MCUtils.executeCommand("server " + input);
                    else {
                        //TODO 跨服切服
                    }
                }
                return 0;
            }
        }

        MCUtils.executeCommand("server " + input);

        return 0;
    }

    private static List<ServerInfo> tryParse() {
        List<String> serverLists = Configs.Lists.SERVER_COMMAND_MAPPING_LIST.getStrings();
        return serverLists.stream()
                          .map(s -> s.split(";", 3))
                          .filter(s -> s.length >= 2)
                          .map(s -> {
                              ServerAddress ipAddr = ServerAddress.parseString(s[0]);
                              String server = s[1];
                              if (s.length == 2)
                                  return new ServerInfo(server, ipAddr);
                              else {
                                  String proxyServer = s[2];
                                  return new ServerInfo(server, ipAddr, proxyServer);
                              }
                          })
                          .toList();
    }

    private static CompletableFuture<Suggestions> suggest(CommandContext<FabricClientCommandSource> context,
                                                          SuggestionsBuilder builder) {
        List<ServerInfo> parsed = tryParse();
        parsed.forEach(info -> {
            if (info.proxyServer() != null)
                builder.suggest(info.server() + "-" + info.proxyServer());
            else
                builder.suggest(info.server());
        });

        return builder.buildFuture();
    }

    public record ServerInfo(String server, ServerAddress ipAddr, String proxyServer) {
        public ServerInfo(String server, ServerAddress ipAddr) {
            this(server, ipAddr, null);
        }
    }
}
