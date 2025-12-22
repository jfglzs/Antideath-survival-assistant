package io.github.jfglzs.feature.totemrestock;

import io.github.jfglzs.mixin.tweakeroo.InventoryUtils_Invoker;
import io.github.jfglzs.utils.MCUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

import static io.github.jfglzs.utils.MCUtils.getMinecraftClient;
import static io.github.jfglzs.utils.PlayerUtils.PlayerInventoryUtils.*;

public class TomtemRestock
{
    public static void trigger()
    {
        PlayerEntity playerEntity = MCUtils.getPlayer();
        int index = searchInventory(Items.TOTEM_OF_UNDYING);

        if (index == -1) return;
        if (index > 8)
        {
            InventoryUtils_Invoker.swapItemToHand(MCUtils.getPlayer(), Hand.OFF_HAND, index);
        }
        else
        {
            int emptySlot = 9;
            clickSlot(getMinecraftClient(), index , emptySlot , SlotActionType.SWAP);
            InventoryUtils_Invoker.swapItemToHand(playerEntity, Hand.OFF_HAND, emptySlot);
            clickSlot(getMinecraftClient(), index , emptySlot , SlotActionType.SWAP);
        }
    }
}
