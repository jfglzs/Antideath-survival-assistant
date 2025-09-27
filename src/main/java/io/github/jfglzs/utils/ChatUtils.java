package io.github.jfglzs.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

import java.util.LinkedList;
import java.util.Queue;

import static io.github.jfglzs.utils.MCUtils.getMinecraftClient;

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
        if (client.player == null) return;
        client.player.sendMessage(Text.of(chat), false);
    }

    public static void sendMessWithSound(String chat , SoundEvent type , float volume, float pitch)
    {
        MinecraftClient client = getMinecraftClient();
        ClientPlayerEntity player = client.player;
        if (player == null) return;
        player.playSound(
                type,
                volume,
                pitch
                );
        player.sendMessage(Text.of(chat), false);
    }

    public static void overLayMess(String text)
    {
        MinecraftClient client = getMinecraftClient();
        client.inGameHud.setOverlayMessage(Text.of(text), false);
    }
}