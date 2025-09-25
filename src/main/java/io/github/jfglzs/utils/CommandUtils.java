package io.github.jfglzs.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import java.util.LinkedList;
import java.util.Queue;

public class CommandUtils
{
    private static final Queue<String> commandQueue = new LinkedList<>();

    static
    {
        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            if (client.player != null && !commandQueue.isEmpty())
            {
                String cmd = commandQueue.poll();
                client.player.networkHandler.sendChatCommand(cmd);
            }
        });
    }

    public static void excuteCommand(String command)
    {
        commandQueue.add(command);
    }
}