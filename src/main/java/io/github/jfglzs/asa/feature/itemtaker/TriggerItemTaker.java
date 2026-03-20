package io.github.jfglzs.asa.feature.itemtaker;

import net.minecraft.item.Item;

public class TriggerItemTaker {
    private static int index = 0;

    public static void trigger() {
//        List<String> list = SWITCH_ITEM_LIST.getStrings();
//        if (list.isEmpty()) return;
//        if (list.size() == index) index = 0;
//        String id = list.get(index);
//        index++; // 列表循环
//
//        Item item = PlayerUtils.transfromToItem(id); //把字符串转换为 Item类型
//        if (!searchAndSwitchItem(item)) MCUtils.ChatUtils.sendMessWithSound("失败" , SoundEvents.ENTITY_VILLAGER_NO , 1 ,1);
    }


//    public static boolean searchAndSwitchItem(Item item) {
//        MinecraftClient client = MCUtils.getMinecraftClient();
//        int index = PlayerUtils.searchInventory(item);
//        if (index == -1) return false;
//        if (MATERIAL_RECYCLER.getBooleanValue()) {
//            MCUtils.ChatUtils.sendMessWithSound("请先关闭材料回输助手!!" , SoundEvents.ENTITY_VILLAGER_NO , 1 ,1);
//            return false;
//        }
//        if (!ScreenUtils.openAndCheckScreen()) return false;
//
//        //#if MC > 12104
//        int button = client.player.getInventory().getSelectedSlot();
//        //#else
//        //$$ int button = client.player.getInventory().selectedSlot;
//        //#endif
//        int id = client.player.currentScreenHandler.slots.get(index).id;
//
//        if (id >= 9) {
//            PlayerUtils.clickSlot(client, id, button, SlotActionType.SWAP);
//        } else {
//            //#if MC > 12104
//            client.player.getInventory().setSelectedSlot(id);
//            //#else
//            //$$ client.player.getInventory().selectedSlot = id;
//            //#endif
//
//        }
//        //#if MC > 12104
//        Objects.requireNonNull(client.getNetworkHandler()).sendPacket(new UpdateSelectedSlotC2SPacket(client.player.getInventory().getSelectedSlot()));
//        //#else
//        //$$ Objects.requireNonNull(client.getNetworkHandler()).sendPacket(new UpdateSelectedSlotC2SPacket(client.player.getInventory().selectedSlot));
//        //#endif
//
//        ScreenUtils.refreshScreen();
//        return true;
//    }
}
