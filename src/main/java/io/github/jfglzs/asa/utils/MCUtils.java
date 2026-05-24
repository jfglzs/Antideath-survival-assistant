package io.github.jfglzs.asa.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.LinkedList;
import java.util.Queue;

import static fi.dy.masa.malilib.util.InventoryUtils.getStoredItems;

public class MCUtils {
    private static final Queue<String> commandQueue = new LinkedList<>();

    public static Minecraft getMinecraftClient()
    {
        return Minecraft.getInstance();
    }

    public static Player getPlayer()
    {
        return getMinecraftClient().player;
    }

    public static Level getWorld()
    {
       return getPlayer().level();
    }

    public static void excuteCommand(String command) {
        commandQueue.add(command);
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static String getItemID(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        return id.toString();
    }

    static {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && !commandQueue.isEmpty()) {
                String cmd = commandQueue.poll();
                client.player.connection.sendCommand(cmd);
            }
        });
    }


    public static class ChatUtils {
        public static void sendMessOnlyClientVisible(String chat) {
            Minecraft client = Minecraft.getInstance();
            if (client.player == null) return;
            client.player.displayClientMessage(Component.nullToEmpty(chat), false);
        }

        public static void sendMessWithSound(String chat , SoundEvent type , float volume, float pitch) {
            chat = "[ASA] " + chat;
            Minecraft client = getMinecraftClient();
            LocalPlayer player = client.player;
            if (player == null) return;
            player.playSound(
                    type,
                    volume,
                    pitch
            );
            player.displayClientMessage(Component.nullToEmpty(chat), false);
        }
    }
}
