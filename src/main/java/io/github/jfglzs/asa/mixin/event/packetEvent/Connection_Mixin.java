package io.github.jfglzs.asa.mixin.event.packetEvent;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.jfglzs.asa.events.SendPacketEvent;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
//? if < 1.21.8 {
/*import net.minecraft.network.PacketSendListener;
 *///?}

@Mixin(Connection.class)
public class Connection_Mixin {
    @WrapMethod(
            method = "sendPacket"
    )
            //? if >= 1.21.8 {
    private void sendPacket(Packet<?> packet, ChannelFutureListener listener, boolean flush, Operation<Void> original) {
        //?} else {
        /*private void sendPacket(Packet<?> packet, PacketSendListener listener, boolean flush, Operation<Void> original) {
         *///?}
        original.call(SendPacketEvent.INSTANCE.update(packet), listener, flush);
    }
}
