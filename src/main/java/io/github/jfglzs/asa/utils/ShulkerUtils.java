package io.github.jfglzs.asa.utils;

import fi.dy.masa.malilib.util.InventoryUtils;
import net.kyrptonaught.quickshulker.network.OpenShulkerPacket;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ShulkerUtils {
    public static void open(int index) {
        if (Mods.isShulkerBoxLoaded) {
            OpenShulkerPacket.sendOpenPacket(index);
        }
        else {
            ChatUtils.sendOverLayMessage(ChatUtils.c("未安装快捷潜影盒"));
        }
    }

    public static boolean findBoxToOpen(ItemStack stack) {
        if (stack == null) return true;

        List<Integer> boxes = PlayerUtils.getAllBoxIndexes(9, 36);
        for (Integer index : boxes) {
            ItemStack boxStack = PlayerUtils.getInventory().get(index);
            if (PlayerUtils.isBoxEmpty(boxStack)) break;
            for (ItemStack boxItemStack : PlayerUtils.getBoxItemStacks(boxStack)) {
                if (stack.isEmpty() || InventoryUtils.areStacksEqualIgnoreDurability(stack, boxItemStack)) {
                    ShulkerUtils.open(index);
                    return true;
                }
            }
        }
        PlayerUtils.closeContainer();
        return false;
    }
}
