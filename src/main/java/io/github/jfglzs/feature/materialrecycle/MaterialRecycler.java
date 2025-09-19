package io.github.jfglzs.feature.materialrecycle;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.List;

import static io.github.jfglzs.config.Configs.MATERIAL_RECYCLER_BLACK_LIST;
import static io.github.jfglzs.config.Configs.MATERIAL_RECYCLER_LIST;
import static io.github.jfglzs.utils.PlayerUtils.PlayerInventoryUtils.*;
import static net.kyrptonaught.quickshulker.client.ClientUtil.CheckAndSend;

public class MaterialRecycler
{
    public static Boolean allowUpdate = true;
    public static int openedBoxSlot = -1;


    public static boolean isWhiteListed(Item item)
    {
        List<String> list = MATERIAL_RECYCLER_LIST.getStrings();
        if (!list.isEmpty())
        {
            for (String id : list)
            {
                //#if MC > 12001
                Identifier identifier = Identifier.ofVanilla(id);
                //#else
                //$$ Identifier identifier = new Identifier("minecraft", id);
                //#endif
                Item listedItem = Registries.ITEM.get(identifier);
                if (item.equals(listedItem))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isBlackListed(Item item)
    {
        List<String> list = MATERIAL_RECYCLER_BLACK_LIST.getStrings();
        if (!list.isEmpty())
        {
            for (String id : list)
            {
                //#if MC > 12001
                Identifier identifier = Identifier.ofVanilla(id);
                //#else
                //$$ Identifier identifier = new Identifier("minecraft", id);
                //#endif
                Item listedItem = Registries.ITEM.get(identifier);
                if (item.equals(listedItem)) return true;
            }
        }
        return false;
    }

    public static void OpenAllBoxes()
    {
//        if(getMinecraftClient().player != null) getMinecraftClient().player.closeHandledScreen();
        List<Integer> list = getAllUnFullShulkerBoxIndexes(getAllShulkerBoxIndexes(41));
        System.out.println(list);
        if (list.isEmpty()) return;


        ItemStack SB = new ItemStack(Items.SHULKER_BOX);
        for (int i : list)
        {
//            if ((allowUpdate))
//            {
                CheckAndSend(SB, i);
                openedBoxSlot = getOpenedBoxEmptySlots(i);
                allowUpdate = false;
//            }
        }
    }
}

