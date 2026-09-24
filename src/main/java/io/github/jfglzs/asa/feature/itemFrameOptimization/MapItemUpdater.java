package io.github.jfglzs.asa.feature.itemFrameOptimization;

import io.github.jfglzs.asa.accessor.IClientPacketListenerAccessor1;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MapItemUpdater {
    public static void tick() {
        if (Configs.Optimizations.OPT_ITEM_FRAME.getBooleanValue()) {
            for (InteractionHand hand : InteractionHand.values())
                update(PlayerUtils.getPlayerHandStack(hand));

            for (ItemStack stack : PlayerUtils.getInventory())
                update(stack);
        }
    }

    public static void update(ItemStack stack) {
        LocalPlayer player = MCUtils.getLocalPlayer();

        if (player == null || ! stack.is(Items.FILLED_MAP))
            return;

        var mapId = stack.get(DataComponents.MAP_ID);

        if (mapId == null)
            return;

        ((IClientPacketListenerAccessor1) player.connection).asa$getMaps().remove(mapId.id());
    }
}
