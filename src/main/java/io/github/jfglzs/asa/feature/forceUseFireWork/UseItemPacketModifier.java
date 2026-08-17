package io.github.jfglzs.asa.feature.forceUseFireWork;

import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.events.SendPacketEvent;
import io.github.jfglzs.asa.utils.MCUtils;
import io.github.jfglzs.asa.utils.PlayerUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;

public class UseItemPacketModifier {
    public static void init() {
        SendPacketEvent.INSTANCE.register(packet -> {
            LocalPlayer player = MCUtils.getLocalPlayer();
            if (Configs.FORCE_USE_FIREWORK.getBooleanValue()
                    && packet instanceof ServerboundUseItemOnPacket
                    && PlayerUtils.getPlayerMainHandStack().is(Items.FIREWORK_ROCKET)
                    && player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
            ) {
                return new ServerboundUseItemPacket(InteractionHand.MAIN_HAND, 2, player.getYRot(), player.getXRot());
            }

            return packet;
        });
    }
}
