package io.github.jfglzs.asa.feature.disablePacketKick;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.CommonPacketTypes;

public final class ASAFakePacket implements Packet<ClientCommonPacketListener> {
    public static final ASAFakePacket INSTANCE = new ASAFakePacket();

    public ASAFakePacket() {
    }

    @Override
    public PacketType<? extends Packet<ClientCommonPacketListener>> type() {
        return CommonPacketTypes.CLIENTBOUND_CUSTOM_PAYLOAD;
    }

    @Override
    public void handle(ClientCommonPacketListener listener) {
    }
}
