package io.github.jfglzs.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

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
}
