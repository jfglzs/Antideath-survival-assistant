package io.github.jfglzs.asa.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;

public class ChatUtils {
    private static final Minecraft client = Minecraft.getInstance();

    public static void sendMessOnlyClientVisible(Component chat) {
        if (client.player == null) return;
        //? if >= 26.1 {
        client.player.sendSystemMessage(chat);
        //?} else {
        /*client.player.displayClientMessage(chat, false);
         *///?}
    }

    public static void sendMessWithSound(Component chat, SoundEvent type, float volume, float pitch) {
        LocalPlayer player = client.player;
        if (player != null) {
            player.playSound(type, volume, pitch);
            //? if >= 26.1 {
            client.player.sendSystemMessage(chat);
            //?} else {
            /*client.player.displayClientMessage(chat, false);
            *///?}
        }
    }

    public static void sendOverLayMessage(Component chat) {
        if (client.player == null) return;
        //? if >= 26.1 {
        client.player.sendOverlayMessage(chat);
        //?} else {
        /*client.player.displayClientMessage(chat, true);
        *///?}
    }

    public static void sendMessageToServer(String chat) {
        if (client.player != null) {
            client.player.connection.sendChat(chat);
        }
    }

    public static Component toComponent(String text) {
        return text == null ? Component.empty() : Component.nullToEmpty(text);
    }
}
