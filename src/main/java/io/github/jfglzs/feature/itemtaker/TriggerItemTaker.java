package io.github.jfglzs.feature.itemtaker;

import io.github.jfglzs.utils.ChatUtils;
import io.github.jfglzs.utils.MCUtils;
import io.github.jfglzs.utils.ScreenUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvents;

import java.util.List;
import java.util.Objects;

import static io.github.jfglzs.config.Configs.*;
import static io.github.jfglzs.utils.PlayerUtils.PlayerInventoryUtils.*;

public class TriggerItemTaker
{
    private static int index = 0;

    public static void trigger()
    {
        List<String> list = SWITCH_ITEM_LIST.getStrings();
        if (list.isEmpty()) return;
        if (list.size() == index) index = 0;
        String id = list.get(index);
        index++; // 列表循环

        Item item = transfromToItem(id); //把字符串转换为 Item类型
        if (!searchAndSwitchItem(item)) ChatUtils.sendMessWithSound("失败" , SoundEvents.ENTITY_VILLAGER_NO , 1 ,1);
    }


    public static boolean searchAndSwitchItem(Item item)
    {
        MinecraftClient client = MCUtils.getMinecraftClient();
        int index = searchInventory(item);
        if (index == -1) return false;
        if (MATERIAL_RECYCLER.getBooleanValue())
        {
            ChatUtils.sendMessWithSound("请先关闭材料回输助手!!" , SoundEvents.ENTITY_VILLAGER_NO , 1 ,1);
            return false;
        }
        if (!MCUtils.openAndCheckScreen()) return false;

        //#if MC > 12104
        int button = client.player.getInventory().getSelectedSlot();
        //#else
        //$$ int button = client.player.getInventory().selectedSlot;
        //#endif
        int id =  client.player.currentScreenHandler.slots.get(index).id;

        if (id >= 9)
        {
            clickSlot(client, id, button, SlotActionType.SWAP);
        }
        else
        {
            //#if MC > 12104
            client.player.getInventory().setSelectedSlot(id);
            //#else
            //$$ client.player.getInventory().selectedSlot = id;
            //#endif

        }
        //#if MC > 12104
        Objects.requireNonNull(client.getNetworkHandler()).sendPacket(new UpdateSelectedSlotC2SPacket(client.player.getInventory().getSelectedSlot()));
        //#else
        //$$ Objects.requireNonNull(client.getNetworkHandler()).sendPacket(new UpdateSelectedSlotC2SPacket(client.player.getInventory().selectedSlot));
        //#endif

        ScreenUtils.refreshScreen();
        return true;
    }
}
