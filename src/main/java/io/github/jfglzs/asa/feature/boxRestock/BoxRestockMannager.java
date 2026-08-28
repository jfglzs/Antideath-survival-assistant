package io.github.jfglzs.asa.feature.boxRestock;

//~ if >= 26.1 'ClickType' -> 'ContainerInput' {

import fi.dy.masa.malilib.util.InventoryUtils;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.Mods;
import io.github.jfglzs.asa.utils.PlayerUtils;
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
        if (Mods.quickshulker) {
            process();
        }
    }

    public static void process() {
        if (! Configs.AUTO_BOX_RESTROKE.getBooleanValue() || context == null || ! PlayerUtils.isSurvivalMode())
            return;

        if (MCUtils.getScreen() instanceof ShulkerBoxScreen boxScreen) {
            var menu = boxScreen.getMenu();
            ItemStack stackHand = context.stackHand;
            for (Slot slot : menu.slots) {
                ItemStack slotItem = slot.getItem();
                if (InventoryUtils.areStacksEqualIgnoreDurability(slotItem, stackHand) && canMove(slotItem)) {
                    int slotIndex = slot.index;
                    int containerId = menu.containerId;
                    LocalPlayer player = MCUtils.getLocalPlayer();
                    if (slotIndex >= 54 && slotIndex <= 62)
                        continue;

                    //~ if >= 1.21.5 'selected' -> 'getSelectedSlot()' {
                    int currentSlot = context.hand == InteractionHand.MAIN_HAND ? player.getInventory()
                                                                                        .getSelectedSlot() + 54 : 45;
                    //~}

                    if (currentSlot == 45) {
                        PlayerUtils.clickSlot(containerId, slotIndex, 40, ContainerInput.SWAP, player);
                    }
                    else {
                        PlayerUtils.clickSlot(containerId, slotIndex, 0, ContainerInput.PICKUP, player);
                        PlayerUtils.clickSlot(containerId, currentSlot, 0, ContainerInput.PICKUP, player);
                        if (stackHand.getMaxStackSize() != 1)
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

        if (Configs.ENABLE_AUTO_BOX_RESTROKE_BLACKLIST.getBooleanValue())
            return ! Configs.isInList(itemID, Configs.AUTO_BOX_RESTROKE_BLACKLIST);
        else if (Configs.ENABLE_AUTO_BOX_RESTROKE_WHITELIST.getBooleanValue())
            return Configs.isInList(itemID, Configs.AUTO_BOX_RESTROKE_WHITELIST);

        return true;
    }

    public record BoxRestockContext(ItemStack stackHand, InteractionHand hand) {
    }
}
//~}