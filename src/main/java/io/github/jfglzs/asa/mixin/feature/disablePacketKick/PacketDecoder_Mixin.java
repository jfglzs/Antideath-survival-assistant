package io.github.jfglzs.asa.mixin.feature.disablePacketKick;

import io.github.jfglzs.asa.config.Configs;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.PacketDecoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PacketDecoder.class)
public class PacketDecoder_Mixin {
    @Inject(
            method = "decode",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/Packet;type()Lnet/minecraft/network/protocol/PacketType;"),
            cancellable = true
    )
    protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> out, CallbackInfo ci) {
        if (Configs.DISABLE_PACKET_KICK.getBooleanValue()) {
            int readableBytes = input.readableBytes();
            if (readableBytes > 0) {
                input.skipBytes(readableBytes);
                ci.cancel();
            }
        }
    }
}
