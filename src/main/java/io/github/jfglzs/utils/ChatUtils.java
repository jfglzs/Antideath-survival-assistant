package io.github.jfglzs.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import java.util.LinkedList;
import java.util.Queue;

public class ChatUtils {
    private static final Queue<String> chatQueue = new LinkedList<>();

    static {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && !chatQueue.isEmpty()) {
                String mess = chatQueue.poll();
                client.player.networkHandler.sendChatMessage(mess);
            }
        });
    }

    public static void sendChat(String chat) {
        chatQueue.add(chat);
    }
}