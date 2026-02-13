package io.github.jfglzs.asa.feature.itemdisplay;

import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static fi.dy.masa.malilib.util.InventoryUtils.getStoredItems;
import static io.github.jfglzs.asa.utils.MCUtils.getPlayer;

public class RemainingItemDisplayer {
    public static int checkRemainCount(Item item) {
        int storedCount = PlayerUtils.getNotEmptyBoxIndexes(PlayerUtils.getAllBoxIndexes(36)).stream()
                .flatMap(i -> getStoredItems(getPlayer().getInventory().getStack(i), 27).stream())
                .filter(j -> j.getItem().equals(item))
                .mapToInt(ItemStack::getCount)
                .sum();

        return storedCount + PlayerUtils.getInventoryItemCount(item);
    }
}
