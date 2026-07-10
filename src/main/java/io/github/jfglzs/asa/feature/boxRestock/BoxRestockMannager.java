package io.github.jfglzs.asa.feature.boxRestock;

import fi.dy.masa.itemscroller.util.InventoryUtils;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.events.OpenScreenEvent;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.Mods;
import io.github.jfglzs.asa.utils.ThreadUtils;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BoxRestockMannager {
    public static BoxRestockContext context = null;

    public static void init() {
        OpenScreenEvent.INSTANCE.register(screen -> {
            if (screen instanceof ShulkerBoxScreen && Mods.isShulkerBoxLoaded) {
                ThreadUtils.runOnClientEndTick(BoxRestockMannager::process);
            }
        });
    }

    //也真是服了 打开快捷潜影盒能给我setScreen 3次 一直提示containerId不匹配
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

//                mc.gameMode.handleContainerInput(menu.containerId, slot.index, button, ContainerInput.PICKUP, player);
//                mc.gameMode.handleContainerInput(player.inventoryMenu.containerId, targetSlot.index, button, ContainerInput.PICKUP, player);
//                mc.gameMode.handleContainerInput(menu.containerId, targetSlot.index, 0, ContainerInput.PICKUP, player);

                    MCUtils.setScreen(null);
                    context = null;
                    return;
                }
            }
        }
    }

    private static boolean canMove(ItemStack stack) {
        Item item = stack.getItem();
        String itemID = MCUtils.getItemID(item);
        if (Configs.ENABLE_AUTO_WASTE_CLEAN_BLACKLIST.getBooleanValue()) {
            return Configs.AUTO_BOX_RESTROKE_BLACKLIST.getStrings().stream().noneMatch(itemID::equals);
        }
        else if (Configs.ENABLE_STRONG_BLOCK_COLLISION_WHITELIST.getBooleanValue()) {
            return Configs.AUTO_BOX_RESTROKE_WHITELIST.getStrings().stream().anyMatch(itemID::equals);
        }
        return false;
    }

    public record BoxRestockContext(ItemStack stackHand) {}
}
