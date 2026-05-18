package io.github.jfglzs.asa.utils;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class CommandUtils {
    public static boolean canUseCommand(String command) {
        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (networkHandler != null) {
            CommandDispatcher<ClientCommandSource> dispatcher = networkHandler.getCommandDispatcher();
            return dispatcher.getRoot().getChild(command) != null;
        }
        return false;
    }
}
