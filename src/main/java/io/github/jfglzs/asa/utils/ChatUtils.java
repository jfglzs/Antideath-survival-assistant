package io.github.jfglzs.asa.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;

public class ChatUtils {
    private static final Minecraft client = Minecraft.getInstance();

    public static void sendMessOnlyClientVisible(String chat) {
        if (client.player == null) return;
        client.player.displayClientMessage(Component.nullToEmpty(chat), false);
    }

    public static void sendMessWithSound(String chat, SoundEvent type, float volume, float pitch) {
        LocalPlayer player = client.player;
        if (player != null) {
            player.playSound(type, volume, pitch);
            player.displayClientMessage(Component.nullToEmpty(chat), false);
        }
    }

    public static void sendMessageToServer(String chat) {
        if (client.player != null) {
            client.player.connection.sendChat(chat);
        }
    }
}
