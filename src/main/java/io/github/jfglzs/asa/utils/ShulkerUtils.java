package io.github.jfglzs.asa.utils;

import fi.dy.masa.malilib.util.InventoryUtils;
import net.kyrptonaught.quickshulker.network.OpenShulkerPacket;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ShulkerUtils {
    public static void open(int index) {
        if (Mods.quickshulker) {
            OpenShulkerPacket.sendOpenPacket(index);
            return;
        }
        ChatUtils.actionBar(ChatUtils.c("未安装快捷潜影盒"));
    }

    public static boolean findBoxToOpen(ItemStack stack) {
        if (stack == null || stack.isEmpty())
            return false;

        List<Integer> boxes = PlayerUtils.getAllBoxIndexes(9, 36);

        for (int index : boxes) {
            ItemStack boxStack = PlayerUtils.getInventory().get(index);
            if (PlayerUtils.isBoxEmpty(boxStack))
                continue;
            for (ItemStack boxItemStack : PlayerUtils.getBoxItemStacks(boxStack)) {
                if (InventoryUtils.areStacksEqualIgnoreDurability(stack, boxItemStack)) {
                    ShulkerUtils.open(index);
                    return true;
                }
            }
        }

        return false;
    }
}
