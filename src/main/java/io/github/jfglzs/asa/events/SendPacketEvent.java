package io.github.jfglzs.asa.events;

import io.github.jfglzs.asa.events.base.ReturnableEvent;
import net.minecraft.network.protocol.Packet;

public class SendPacketEvent extends ReturnableEvent<Packet<?>> {
    public static final SendPacketEvent INSTANCE = new SendPacketEvent();
}
