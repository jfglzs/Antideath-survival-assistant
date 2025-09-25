package io.github.jfglzs.feature.materialrecycle;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.List;

import static io.github.jfglzs.config.Configs.MATERIAL_RECYCLER_BLACK_LIST;
import static io.github.jfglzs.config.Configs.MATERIAL_RECYCLER_LIST;
import static io.github.jfglzs.utils.FabricUtils.isModLoaded;
import static io.github.jfglzs.utils.PlayerUtils.PlayerInventoryUtils.*;
import static net.kyrptonaught.quickshulker.client.ClientUtil.CheckAndSend;


public class MaterialRecycler
{
    public static boolean allowUpdate = true;
    public static int openedBoxSlot = -1;
    static List<String> bl = List.of(
            "air",
            "shulker_box",
            "white_shulker_box",
            "orange_shulker_box",
            "magenta_shulker_box",
            "light_blue_shulker_box",
            "yellow_shulker_box",
            "lime_shulker_box",
            "pink_shulker_box",
            "gray_shulker_box",
            "light_gray_shulker_box",
            "cyan_shulker_box",
            "purple_shulker_box",
            "blue_shulker_box",
            "brown_shulker_box",
            "green_shulker_box",
            "red_shulker_box",
            "black_shulker_box"
    );



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
        if (isBL(item)) return true;

        for (String id : list)
        {
            //#if MC > 12001
            if (id.contains("minecraft")) return false;
            Identifier identifier = Identifier.ofVanilla(id);
            //#else
            //$$ Identifier identifier = new Identifier("minecraft", id);
            //#endif
            Item listedItem = Registries.ITEM.get(identifier);
            if (item.equals(listedItem)) return true;
        }

        return false;
    }

    public static void openAllBoxes()
    {
        if (!isModLoaded("quickshulker")) return;
        List<Integer> list = getUnFullBoxIndexes(getAllBoxIndexes(35));
        if (list.isEmpty()) return;

        for (int i : list)
        {
            if (allowUpdate)
            {
//                System.out.println(list);
                CheckAndSend(new ItemStack(Items.SHULKER_BOX), i);
                openedBoxSlot = getOpenedBoxEmptySlots(i);
                allowUpdate = false;
            }
        }
    }

    private static boolean isBL(Item item)
    {
        for (String id : bl)
        {
            //#if MC > 12001
            if (id.contains("minecraft")) return false;
            Identifier identifier = Identifier.ofVanilla(id);
            //#else
            //$$ Identifier identifier = new Identifier("minecraft", id);
            //#endif
            Item listedItem = Registries.ITEM.get(identifier);
            if (item.equals(listedItem)) return true;
        }

        return false;
    }
}

