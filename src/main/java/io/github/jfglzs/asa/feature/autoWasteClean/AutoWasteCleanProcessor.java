package io.github.jfglzs.asa.feature.autoWasteClean;

import fi.dy.masa.itemscroller.util.InventoryUtils;
import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.events.OpenScreenEvent;
import io.github.jfglzs.asa.feature.boxRestock.BoxRestockMannager;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class AutoWasteCleanProcessor {
    public static void init() {
        OpenScreenEvent.INSTANCE.register(s -> process());
    }

    /**
     * InventoryScreen(玩家背包)
     * ChestMenu(箱子末影箱)
     */
    public static void process() {
        if (Configs.ENABLE_AUTO_WASTE_CLEAN.getBooleanValue()) {
            //兼容快捷盒子补货
            var screen = MCUtils.getScreen();

            if (screen instanceof ShulkerBoxScreen && BoxRestockMannager.context != null) return;

            if (screen instanceof AbstractContainerScreen<?> container) {
                var menu = container.getMenu();
                var player = MCUtils.getMinecraft().player;
                if (! PlayerUtils.isSurvivalMode(player)) return;
                String mode = Configs.AUTO_WASTE_CLEAN_MODE.getStringValue();

                for (Slot slot : menu.slots) {
                    ItemStack stack = slot.getItem();
                    boolean isInv = slot.container instanceof Inventory;
                    if (stack.isEmpty() || !shouldDrop(stack)) continue;

                    if (menu instanceof InventoryMenu && mode.equals("丢出物品")) {
                        InventoryUtils.dropStack(container, slot.index);
                        AsaMod.debugMessage(() -> "Dropped Inventory container for slot " + slot.index);
                    }
                    else if (menu instanceof ChestMenu && mode.equals("转移至容器") && isInv) {
                        InventoryUtils.tryMoveStacks(slot, container, true, true, false);
                        AsaMod.debugMessage(() -> "Moved Inventory Item to container for slot " + slot.index);
                    }

                    ChatUtils.actionBar(ChatUtils.c("清理完成"));
                }

                player.closeContainer();
            }
        }
    }

    private static boolean shouldDrop(ItemStack stack) {
        String id = MCUtils.getItemID(stack.getItem());

        if (! PlayerUtils.isShulkerBox(stack)) {
            if (Configs.ENABLE_AUTO_WASTE_CLEAN_BLACKLIST.getBooleanValue())
                return ! Configs.isInList(id, Configs.AUTO_WASTE_CLEAN_BLACKLIST);
            else if (Configs.ENABLE_AUTO_WASTE_CLEAN_WHITELIST.getBooleanValue())
                return Configs.isInList(id, Configs.AUTO_WASTE_CLEAN_WHITELIST);

            return true;
        }

        for (ItemStack boxStack : PlayerUtils.getBoxItemStacks(stack)) {
            if (boxStack.isEmpty()) continue;
            if (shouldDrop(boxStack)) return true;
        }

        return true;
    }

    public static void saveItemToList() {
        Set<String> items = new HashSet<>();
        for (ItemStack stack : PlayerUtils.getInventory()) {
            if (stack.isEmpty()) continue;
            if (PlayerUtils.isShulkerBox(stack)) {
                for (ItemStack boxStack : PlayerUtils.getBoxItemStacks(stack)) {
                    if (stack.isEmpty()) continue;
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
            ChatUtils.actionBar(ChatUtils.c("成功将玩家物品栏保存至黑名单"));
        }
        else if (Configs.ENABLE_AUTO_WASTE_CLEAN_WHITELIST.getBooleanValue()) {
            Configs.AUTO_WASTE_CLEAN_WHITELIST.setStrings(strings);
            ChatUtils.actionBar(ChatUtils.c("成功将玩家物品栏保存至白名单"));
        }

        AsaMod.debugMessage(() -> "Saved Items to List \n " + strings);
    }
}
