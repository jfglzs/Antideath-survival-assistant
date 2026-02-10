package io.jfglzs.asa.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.Queue;

import static fi.dy.masa.malilib.util.InventoryUtils.getStoredItems;

public class MCUtils {
    private static final Queue<String> commandQueue = new LinkedList<>();

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

    public static int getInventorySlotAmount(ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem) {
            Block block = ((BlockItem) itemStack.getItem()).getBlock();
            if (block instanceof ShulkerBoxBlock || block instanceof ChestBlock || block instanceof BarrelBlock) {
                return 27;
            } else if (block instanceof AbstractFurnaceBlock) {
                return 3;
            } else if (block instanceof DispenserBlock) {
                return 9;
            } else if (block instanceof HopperBlock || block instanceof BrewingStandBlock) {
                return 5;
            }
        }
        return -1;
    }

    public static void excuteCommand(String command) {
        commandQueue.add(command);
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    static {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && !commandQueue.isEmpty()) {
                String cmd = commandQueue.poll();
                client.player.networkHandler.sendChatCommand(cmd);
            }
        });
    }

    public static class ChatUtils {
        public static void sendMessOnlyClientVisible(String chat) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;
            client.player.sendMessage(Text.of(chat), false);
        }

        public static void sendMessWithSound(String chat , SoundEvent type , float volume, float pitch) {
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

        public static void overLayMess(String text) {
            MinecraftClient client = getMinecraftClient();
            client.inGameHud.setOverlayMessage(Text.of(text), false);
        }
    }

}
