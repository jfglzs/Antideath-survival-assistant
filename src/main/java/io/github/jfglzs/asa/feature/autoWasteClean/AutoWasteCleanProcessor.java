package io.github.jfglzs.asa.feature.autoWasteClean;

import fi.dy.masa.itemscroller.util.InventoryUtils;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class AutoWasteCleanProcessor {
    /**
     * InventoryScreen(玩家背包)
     * ChestMenu(箱子末影箱)
     */
    public static void process(AbstractContainerScreen<?> container) {
        if (Configs.ENABLE_AUTO_WASTE_CLEAN.getBooleanValue()) {
            var menu = container.getMenu();
            var player = MCUtils.getMinecraft().player;
            if (!PlayerUtils.isSurvivalMode(player)) return;
            String mode = Configs.AUTO_WASTE_CLEAN_MODE.getStringValue();

            for (Slot slot : menu.slots) {

                ItemStack stack = slot.getItem();
                if (isStackEmpty(stack) || shouldKeep(stack) || !(slot.container instanceof Inventory)) continue;

                if (menu instanceof InventoryMenu && mode.equals("丢出物品")) {
                    InventoryUtils.dropStack(container, slot.index);
                    AsaMod.debugMessage("Dropped Inventory container for slot " + slot.index);
                }
                else if (menu instanceof ChestMenu && mode.equals("转移至容器")) {
                    InventoryUtils.tryMoveStacks(slot, container, true, true, false);
                    AsaMod.debugMessage("Moved Inventory Item to container for slot " + slot.index);
                }
            }
        }
    }

    private static boolean isStackEmpty(ItemStack stack) {
        return stack.isEmpty();
    }

    private static boolean shouldKeep(ItemStack stack) {
        String id = MCUtils.getItemID(stack.getItem());

        if (!PlayerUtils.isShulkerBox(stack)) {
            if (Configs.ENABLE_AUTO_WASTE_CLEAN_BLACKLIST.getBooleanValue())
                return Configs.AUTO_WASTE_CLEAN_BLACKLIST.getStrings().contains(id);
            else if (Configs.ENABLE_AUTO_WASTE_CLEAN_WHITELIST.getBooleanValue())
                return !Configs.AUTO_WASTE_CLEAN_WHITELIST.getStrings().contains(id);

            return false;
        }

        for (ItemStack boxStack : PlayerUtils.getBoxItemStacks(stack)) {
            if (isStackEmpty(boxStack)) continue;
            if (shouldKeep(boxStack)) return true;
        }

        return false;
    }

    public static void saveItemToList() {
        Set<String> items = new HashSet<>();
        for (ItemStack stack : PlayerUtils.getInventory()) {
            if (isStackEmpty(stack)) continue;
            if (PlayerUtils.isShulkerBox(stack)) {
                for (ItemStack boxStack : PlayerUtils.getBoxItemStacks(stack)) {
                    if (isStackEmpty(stack)) continue;
                    items.add(MCUtils.getItemID(boxStack.getItem()));
                }
            }
            else {
                items.add(MCUtils.getItemID(stack.getItem()));
            }
        }

        List<String> strings = items.stream().toList();

        if (Configs.ENABLE_AUTO_WASTE_CLEAN_BLACKLIST.getBooleanValue()) {
            Configs.AUTO_WASTE_CLEAN_BLACKLIST.setStrings(strings);
            ChatUtils.sendOverLayMessage(ChatUtils.toComponent("成功将玩家物品栏保存至黑名单"));
        }
        else if (Configs.ENABLE_AUTO_WASTE_CLEAN_WHITELIST.getBooleanValue()) {
            Configs.AUTO_WASTE_CLEAN_WHITELIST.setStrings(strings);
            ChatUtils.sendOverLayMessage(ChatUtils.toComponent("成功将玩家物品栏保存至白名单"));
        }

        AsaMod.debugMessage("Saved Items to List \n " + strings);
    }
}
