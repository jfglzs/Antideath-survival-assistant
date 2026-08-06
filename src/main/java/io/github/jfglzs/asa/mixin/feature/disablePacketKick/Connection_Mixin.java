package io.github.jfglzs.asa.mixin.feature.disablePacketKick;

import io.github.jfglzs.asa.config.Configs;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class Connection_Mixin {
    @Inject(
            method = "sendPacket",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sendPacket(Packet<?> packet, ChannelFutureListener listener, boolean flush, CallbackInfo ci) {
        if (Configs.DISABLE_PACKET_KICK_PREVENT_CUSTOM_PAYLOAD.getBooleanValue() && packet instanceof ServerboundCustomPayloadPacket) {
            ci.cancel();
        }
    }
}