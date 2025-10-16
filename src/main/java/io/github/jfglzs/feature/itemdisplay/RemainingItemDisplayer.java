package io.github.jfglzs.feature.itemdisplay;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

import static fi.dy.masa.malilib.util.InventoryUtils.getStoredItems;
import static io.github.jfglzs.utils.MCUtils.getPlayer;
import static io.github.jfglzs.utils.PlayerUtils.PlayerInventoryUtils.*;

public class RemainingItemDisplayer
{
    public static int checkRemainCount(Item item)
    {
        int storedCount = getNotEmptyBoxIndexes(getAllBoxIndexes(36)).stream()
                .flatMap(i -> getStoredItems(getPlayer().getInventory().getStack(i), 27).stream())
                .filter(j -> j.getItem().equals(item))
                .mapToInt(ItemStack::getCount)
                .sum();

        return storedCount + getInventoryItemCount(item);
    }
}
