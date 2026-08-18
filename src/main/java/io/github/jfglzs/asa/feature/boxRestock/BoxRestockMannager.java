package io.github.jfglzs.asa.feature.boxRestock;

//~ if >= 26.1 'ClickType' -> 'ContainerInput' {

import fi.dy.masa.itemscroller.util.InventoryUtils;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.Mods;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BoxRestockMannager {
    public static BoxRestockContext context = null;

    public static void run() {
        if (Mods.quickshulker && Mods.item_scroller) {
            process();
        }
    }

    public static void process() {
        if (context == null || ! Configs.AUTO_BOX_RESTROKE.getBooleanValue()) return;

        if (MCUtils.getScreen() instanceof ShulkerBoxScreen boxScreen) {
            var menu = boxScreen.getMenu();
            for (Slot slot : menu.slots) {
                ItemStack slotItem = slot.getItem();
                ItemStack stackHand = context.stackHand;
                if (fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreDurability(slotItem, stackHand) && canMove(slotItem)) {
                    int slotIndex = slot.index;

                    if (slotIndex >= 54 && slotIndex <= 62) continue;

                    int containerId = menu.containerId;
                    Minecraft mc = MCUtils.getMinecraft();
                    LocalPlayer player = mc.player;

                    //~ if >= 1.21.5 'selected' -> 'getSelectedSlot()' {
                    int currentSlot = context.hand == InteractionHand.MAIN_HAND ? player.getInventory().getSelectedSlot() + 54 : 45;
                    //~}

                    if (currentSlot == 45) {
                        PlayerUtils.clickSlot(containerId, slotIndex, 40, ContainerInput.SWAP, player);
                    }
                    else {
                        PlayerUtils.clickSlot(containerId, slotIndex, 0, ContainerInput.PICKUP, player);
                        PlayerUtils.clickSlot(containerId, currentSlot, 0, ContainerInput.PICKUP, player);
                        PlayerUtils.clickSlot(containerId, slotIndex, 0, ContainerInput.PICKUP, player);
                    }
                    break;
                }
            }
            PlayerUtils.closeContainer();
            context = null;
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

    public record BoxRestockContext(ItemStack stackHand, InteractionHand hand) {
    }
}
//~}