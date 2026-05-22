package io.github.jfglzs.asa.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class CommandUtils {
    public static boolean canUseCommand(String command) {
        ClientPlayNetworkHandler networkHandler = MinecraftClient.getInstance().getNetworkHandler();
        if (networkHandler != null) {
            var dispatcher = networkHandler.getCommandDispatcher();
            return dispatcher.getRoot().getChild(command) != null;
        }
        return false;
    }
}
