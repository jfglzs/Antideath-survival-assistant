package io.github.jfglzs.asa.feature.boxRestock;

import fi.dy.masa.itemscroller.util.InventoryUtils;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.Mods;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BoxRestockMannager {
    public static BoxRestockContext context = null;

    public static void run() {
        if (Mods.isShulkerBoxLoaded) {
            process();
        }
    }

    public static void process() {
        if (context == null || !Configs.AUTO_BOX_RESTROKE.getBooleanValue()) return;

        if (MCUtils.getScreen() instanceof ShulkerBoxScreen boxScreen) {
            var menu = boxScreen.getMenu();
            for (Slot slot : menu.slots) {
                ItemStack slotItem = slot.getItem();
                ItemStack stackHand = context.stackHand;
                if (fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreDurability(slotItem, stackHand) && canMove(slotItem)) {
                    //过滤hotbar防止自己补自己
                    if (slot.index >= 54 && slot.index <= 62) continue;
                    InventoryUtils.tryMoveStacks(slot, boxScreen, true, true, true);
                    MCUtils.getLocalPlayer().closeContainer();
                    context = null;
                    return;
                }
            }
        }
    }

    private static boolean canMove(ItemStack stack) {
        Item item = stack.getItem();
        String itemID = MCUtils.getItemID(item);
        if (Configs.ENABLE_AUTO_BOX_RESTROKE_BLACKLIST.getBooleanValue()) {
            return Configs.AUTO_BOX_RESTROKE_BLACKLIST.getStrings().stream().noneMatch(itemID::equals);
        }
        else if (Configs.ENABLE_AUTO_BOX_RESTROKE_WHITELIST.getBooleanValue()) {
            return Configs.AUTO_BOX_RESTROKE_WHITELIST.getStrings().stream().anyMatch(itemID::equals);
        }
        return true;
    }

    public record BoxRestockContext(ItemStack stackHand) {}
}
