package io.github.jfglzs.asa.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

public class CommandUtils {
    public static boolean canUseCommand(String command) {
        ClientPacketListener networkHandler = Minecraft.getInstance().getConnection();
        if (networkHandler != null) {
            var dispatcher = networkHandler.getCommands();
            return dispatcher.getRoot().getChild(command) != null;
        }
        return false;
    }
}
