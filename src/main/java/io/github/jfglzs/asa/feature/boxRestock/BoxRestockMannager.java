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
    private static boolean canRestock = false;
    private static int tickCount = 0;

    public static void tick(Minecraft mc) {
        if (canRestock && mc.player != null) {
            tickCount++;
            if (tickCount % 4 == 0) {
                fi.dy.masa.tweakeroo.util.InventoryUtils.preRestockHand(mc.player, InteractionHand.OFF_HAND, true);
                tickCount = 0;
                canRestock = false;
            }
        }
    }

    public static void run() {
        if (Mods.quickshulker && Mods.item_scroller) {
            process();
        }
    }

    public static void process() {
        if (context == null || canRestock || ! Configs.AUTO_BOX_RESTROKE.getBooleanValue()) return;

        if (MCUtils.getScreen() instanceof ShulkerBoxScreen boxScreen) {
            var menu = boxScreen.getMenu();
            for (Slot slot : menu.slots) {
                ItemStack slotItem = slot.getItem();
                ItemStack stackHand = context.stackHand;
                if (fi.dy.masa.malilib.util.InventoryUtils.areStacksEqualIgnoreDurability(slotItem, stackHand) && canMove(slotItem)) {
                    //过滤hotbar防止自己补自己
                    if (slot.index >= 54 && slot.index <= 62) continue;
                    int containerId = menu.containerId;
                    Minecraft mc = MCUtils.getMinecraft();
                    LocalPlayer player = mc.player;

                    //~ if >= 1.21.5 'selected' -> 'getSelectedSlot()' {
                    int currentSlot = context.hand == InteractionHand.MAIN_HAND ? player.getInventory().getSelectedSlot() + 54 : 45;
                    //~}

                    if (currentSlot == 45) {
                        InventoryUtils.tryMoveStacks(slot, boxScreen, true, true, true);
                        canRestock = true;
                    }
                    else {
                        PlayerUtils.clickSlot(containerId, slot.index, 0, ContainerInput.PICKUP, player);
                        PlayerUtils.clickSlot(containerId, currentSlot, 0, ContainerInput.PICKUP, player);
                        PlayerUtils.clickSlot(containerId, slot.index, 0, ContainerInput.PICKUP, player);
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