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
        int count = 0;
        List<Integer> boxes = getNotEmptyBoxIndexes(getAllBoxIndexes(35));
        for (int i : boxes)
        {
            DefaultedList<ItemStack> IS = getStoredItems(getPlayer().getInventory().getStack(i) , 27);
            for (ItemStack j : IS)
            {
                if (j.getItem().equals(item))
                {
                    count += j.getCount();
                }
            }
        }

        count += getInventoryItemCount(item);

        return count;
    }



}
