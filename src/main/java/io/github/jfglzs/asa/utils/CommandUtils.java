package io.github.jfglzs.asa.utils;

//~ if >= 26.1 'ClientCommandManager' -> 'ClientCommands' {
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
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

    public static LiteralArgumentBuilder<FabricClientCommandSource> literal(String string) {
        return ClientCommands.literal(string);
    }

    public static RequiredArgumentBuilder<FabricClientCommandSource, ?> argument(String name, ArgumentType<?> type) {
        return ClientCommands.argument(name, type);
    }
    //~}
}
