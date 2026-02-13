package io.github.jfglzs.asa.feature.totemrestock;

import io.github.jfglzs.asa.mixin.tweakeroo.InventoryUtils_Invoker;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

import static io.github.jfglzs.asa.utils.MCUtils.getMinecraftClient;

public class TomtemRestock {
    public static void trigger() {
        PlayerEntity playerEntity = MCUtils.getPlayer();
        int index = PlayerUtils.searchInventory(Items.TOTEM_OF_UNDYING);

        if (index == -1) return;
        if (index > 8) {
            InventoryUtils_Invoker.swapItemToHand(MCUtils.getPlayer(), Hand.OFF_HAND, index);
        } else {
            int emptySlot = 9;
            PlayerUtils.clickSlot(getMinecraftClient(), index , emptySlot , SlotActionType.SWAP);
            InventoryUtils_Invoker.swapItemToHand(playerEntity, Hand.OFF_HAND, emptySlot);
            PlayerUtils.clickSlot(getMinecraftClient(), index , emptySlot , SlotActionType.SWAP);
        }
    }
}
