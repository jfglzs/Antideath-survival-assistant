package io.github.jfglzs.asa.feature.disablePacketKick;

import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.events.SendPacketEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;

public class PacketHandler {
    public static void init() {
        SendPacketEvent.INSTANCE.register(packet -> {
            if (packet instanceof ServerboundCustomPayloadPacket
                    && Configs.DISABLE_PACKET_KICK_PREVENT_CUSTOM_PAYLOAD.getBooleanValue()
                    && FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT
            ) {
                return ASAFakePacket.INSTANCE;
            }
            return packet;
        });
    }
}
