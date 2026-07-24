package io.github.jfglzs.asa.feature.boxSplitter;

import com.google.common.util.concurrent.RateLimiter;
import fi.dy.masa.itemscroller.util.InventoryUtils;
import io.github.jfglzs.asa.utils.ChatUtils;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.ShulkerUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BoxSplitter {
    private static final RateLimiter LIMITER = RateLimiter.create(3.0);
    private static ItemStack itemToSplit = ItemStack.EMPTY;
    private static boolean isRunning = false;
    private static boolean canOpenBox = false;

    public static void addTask(ItemStack stack) {
        if (isRunning) {
            isRunning = false;
            itemToSplit = ItemStack.EMPTY;
            ChatUtils.sendOverLayMessage(ChatUtils.c("已停止"));
            return;
        }

        if (stack.isEmpty()) {
            ChatUtils.sendOverLayMessage(ChatUtils.c("物品不可为空"));
            return;
        }

        isRunning = true;
        itemToSplit = stack.copy();
        run();
    }

    public static void run() {
        if (!isRunning || itemToSplit.isEmpty()) return;

        Screen screen = MCUtils.getScreen();
        if (screen instanceof ShulkerBoxScreen boxScreen) {
            var menu = boxScreen.getMenu();
            for (Slot slot : menu.slots) {
                ItemStack item = slot.getItem();
                if (item.isEmpty() || slot.container instanceof Inventory) continue;
                if (fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreDurability(itemToSplit, item)) {
                    InventoryUtils.dropStack(boxScreen, slot.index);
                }
            }
        }
        canOpenBox = true;
    }

    public static void tick() {
        if (LIMITER.tryAcquire() && canOpenBox) {
            if (!ShulkerUtils.findBoxToOpen(itemToSplit)) {
                ChatUtils.sendOverLayMessage(ChatUtils.c("分离完成"));
                itemToSplit = ItemStack.EMPTY;
                isRunning = false;
                canOpenBox = false;
            }
        }
    }
}
