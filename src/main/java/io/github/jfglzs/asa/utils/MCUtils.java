package io.github.jfglzs.asa.utils;

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

import static fi.dy.masa.malilib.util.InventoryUtils.getStoredItems;

public class MCUtils {
    private static final Minecraft mc = Minecraft.getInstance();

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

    public static void executeCommand(String command) {
        if (mc.player != null && command != null) {
            mc.player.connection.sendCommand(command);
        }
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static String getItemID(Item item) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        return id.toString();
    }

    public static class ChatUtils {
        private static final Minecraft client = Minecraft.getInstance();


        public static void sendMessOnlyClientVisible(String chat) {
            if (client.player == null) return;
            client.player.displayClientMessage(Component.nullToEmpty(chat), false);
        }

        public static void sendMessWithSound(String chat , SoundEvent type , float volume, float pitch) {
            chat = "[ASA] " + chat;
            LocalPlayer player = client.player;
            if (player != null) {
                player.playSound(type, volume, pitch);
                player.displayClientMessage(Component.nullToEmpty(chat), false);
            }
        }

        public static void sendMessageToServer(String chat) {
            if (client.player != null) {
                client.player.connection.sendChat(chat);
            }
        }
    }
}
