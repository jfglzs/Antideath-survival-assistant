package io.github.jfglzs.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.world.World;

import static io.github.jfglzs.utils.ScreenUtils.client;
import static io.github.jfglzs.utils.ScreenUtils.openInventoryScreen;

public class MCUtils {

    public static MinecraftClient getMinecraftClient()
    {
        return MinecraftClient.getInstance();
    }

    public static PlayerEntity getPlayer()
    {
        return getMinecraftClient().player;
    }

    public static World getWorld()
    {
       return getPlayer().getWorld();
    }

    public static boolean openAndCheckScreen() {
        client = getMinecraftClient();
        if (client.currentScreen == null) openInventoryScreen();
        if (client.player == null || client.interactionManager == null) return false;
        ScreenHandler handler = client.player.currentScreenHandler;
        if (handler == null) return false;

        return true;
    }
}
