package io.github.jfglzs.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

public class ScreenUtils
{
    static MinecraftClient client = MinecraftClient.getInstance();

    public static void refreshScreen()
    {
        if (client.player == null) return;
        client.setScreen(new InventoryScreen(client.player));
        closeScreen();
    }

    public static void closeScreen()
    {
        if (client.player == null) return;
        client.player.closeHandledScreen();
    }
}
