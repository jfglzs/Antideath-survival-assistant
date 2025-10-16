package io.github.jfglzs.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.screen.ScreenHandler;

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

    public static ScreenHandler openAndGetHandle(Screen screen)
    {
        client.setScreen(screen);
        if (hasNotPlayer()) return null;
        return client.player.currentScreenHandler;
    }


}
