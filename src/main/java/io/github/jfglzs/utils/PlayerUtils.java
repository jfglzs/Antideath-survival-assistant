package io.github.jfglzs.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class PlayerUtils {
    private static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }

    private static ClientPlayerEntity getPlayer() {
        return getClient().player;
    }

    private static World getWorld() {
        return getClient().world;
    }

    private static boolean isPlayerOrWorldEmpty() {
        return getPlayer() != null && getWorld() != null;
    }

    public static int getEquipmentDuration(EquipmentSlot slot) {
        if (isPlayerOrWorldEmpty()) {
            ItemStack stack = getPlayer().getEquippedStack(slot);
            return stack.getMaxDamage() - stack.getDamage();
        }
        return -1;
    }

    public static int getElytraDuration() {
        if (isPlayerOrWorldEmpty()) {
            ItemStack stack = getPlayer().getEquippedStack(EquipmentSlot.CHEST);
            if (stack.isOf(Items.ELYTRA)) {
                return stack.getMaxDamage() - stack.getDamage();
            }
        }
        return -1;
    }
}