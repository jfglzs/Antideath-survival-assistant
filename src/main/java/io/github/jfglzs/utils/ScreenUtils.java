package io.github.jfglzs.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

public class ScreenUtils
{
    static MinecraftClient client = MinecraftClient.getInstance();

    public static void refreshScreen()
    {
        openInventoryScreen();
        closeScreen();
    }

    public static void closeScreen()
    {
        if (hasNotPlayer()) return;
        client.player.closeHandledScreen();
    }

    public static void openInventoryScreen()
    {
        if (hasNotPlayer()) return;
        client.setScreen(new InventoryScreen(client.player));
    }

    public static boolean hasNotPlayer()
    {
        return client.player == null;
    }


}
