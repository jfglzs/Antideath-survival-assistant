package io.github.jfglzs.feature.itemtaker;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvents;

import java.util.List;

import static io.github.jfglzs.config.Configs.*;
import static io.github.jfglzs.utils.ChatUtils.sendMessWithSound;
import static io.github.jfglzs.utils.MCUtils.getMinecraftClient;
import static io.github.jfglzs.utils.PlayerUtils.PlayerInventoryUtils.searchInventory;
import static io.github.jfglzs.utils.PlayerUtils.PlayerInventoryUtils.transfromToItem;
import static io.github.jfglzs.utils.ScreenUtils.openInventoryScreen;
import static io.github.jfglzs.utils.ScreenUtils.refreshScreen;

public class TriggerItemTaker
{
    private static int index = 0;

    public static void triggered()
    {
        List<String> list = SWITCH_ITEM_LIST.getStrings();
        if (list.isEmpty()) return;
        if (list.size() == index) index = 0;
        String id = list.get(index);
        index++; // 列表循环

        Item item = transfromToItem(id); //把字符串转换为 Item类型
        if (!searchAndSwitchItem(item)) sendMessWithSound("失败" , SoundEvents.ENTITY_VILLAGER_NO , 1 ,1);
    }


    public static boolean searchAndSwitchItem(Item item)
    {
        MinecraftClient client = getMinecraftClient();
        int index = searchInventory(item);

        if (index == -1) return false;

        if (MATERIAL_RECYCLER.getBooleanValue())
        {
            sendMessWithSound("请先关闭材料回输助手!!" , SoundEvents.ENTITY_VILLAGER_NO , 1 ,1);
            return false;
        }

        openInventoryScreen();
        if (client.currentScreen == null) return false;
        if (client.player == null || client.interactionManager == null) return false;
        ScreenHandler handler = client.player.currentScreenHandler;
        if (handler == null) return false;

        int button = client.player.getInventory().getSelectedSlot();
        int id =  handler.slots.get(index).id;

        if (id > 9)
        {
            client.interactionManager.clickSlot(handler.syncId, id , button, SlotActionType.SWAP, client.player);
        }
        else
        {
            client.player.getInventory().setSelectedSlot(id);
        }

        refreshScreen();
        return true;
    }
}
