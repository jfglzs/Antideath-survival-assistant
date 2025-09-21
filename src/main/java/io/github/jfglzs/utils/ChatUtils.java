package io.github.jfglzs.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

import java.util.LinkedList;
import java.util.Queue;

public class ChatUtils
{
    private static final Queue<String> chatQueue = new LinkedList<>();

    static
    {
        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            if (client.player != null && !chatQueue.isEmpty())
            {
                String mess = chatQueue.poll();
                client.player.networkHandler.sendChatMessage(mess);
            }
        });
    }

    public static void sendChat(String chat)
    {
        chatQueue.add(chat);
    }

    public static void sendMessOnlyClientVisible(String chat)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        client.player.sendMessage(Text.of(chat), false);
    }

    public static void sendMessWithSound(String chat , SoundEvent type , float volume, float pitch)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        player.playSound(
                type,
                volume,
                pitch
                );
        client.player.sendMessage(Text.of(chat), false);
    }
}