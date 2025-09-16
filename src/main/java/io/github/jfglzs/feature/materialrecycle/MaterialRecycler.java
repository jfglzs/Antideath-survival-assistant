package io.github.jfglzs.feature.materialrecycle;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.List;

import static io.github.jfglzs.AsaMod.shouldOpenBox;
import static io.github.jfglzs.config.Configs.MATERIAL_RECYCLER_BLACK_LIST;
import static io.github.jfglzs.config.Configs.MATERIAL_RECYCLER_LIST;
import static io.github.jfglzs.utils.MCUtils.getPlayer;
import static io.github.jfglzs.utils.PlayerUtils.PlayerInventoryUtils.getAllShulkerBoxIndexes;
import static io.github.jfglzs.utils.PlayerUtils.PlayerInventoryUtils.getAllUnFullShulkerBoxIndexes;
import static net.kyrptonaught.quickshulker.client.ClientUtil.CheckAndSend;

public class MaterialRecycler
{
    public static Boolean allowUpdate = true;


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
        List<Integer> list = getAllUnFullShulkerBoxIndexes(getAllShulkerBoxIndexes(41));
        if (list.isEmpty()) return;

        for (int i : list)
        {
            if ((shouldOpenBox() && allowUpdate)) CheckAndSend(getPlayer().getInventory().getStack(i) , i);
            allowUpdate = false;
        }
    }
}

