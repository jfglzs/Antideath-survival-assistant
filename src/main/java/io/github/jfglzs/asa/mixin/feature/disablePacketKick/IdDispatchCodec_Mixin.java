package io.github.jfglzs.asa.mixin.feature.disablePacketKick;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.feature.disablePacketKick.ASAFakePacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.IdDispatchCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.common.ClientCommonPacketListener;
import net.minecraft.network.protocol.common.CommonPacketTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(IdDispatchCodec.class)
public class IdDispatchCodec_Mixin<B extends ByteBuf, V> {
    @Unique
    private static final ASAFakePacket asa$FAKEPACKET = new ASAFakePacket();

    @WrapMethod(
            method = "decode(Lio/netty/buffer/ByteBuf;)Ljava/lang/Object;"
    )
    public V decode(B input, Operation<V> original) {
        ByteBuf buf = input.markReaderIndex();
        V result;
        try {
             result = original.call(input);
        }
        catch (Exception e) {
            if (Configs.DISABLE_PACKET_KICK.getBooleanValue()) {
                buf.skipBytes(buf.readableBytes());
                return (V) asa$FAKEPACKET;
            }
            throw e;
        }
        return result;
    }

    @WrapMethod(
            method = "encode(Lio/netty/buffer/ByteBuf;Ljava/lang/Object;)V"
    )
    public void decode(B output, V value, Operation<Void> original) {
        ByteBuf buf = output.markReaderIndex();
        try {
            original.call(output, value);
        }
        catch (Exception e) {
            if (Configs.DISABLE_PACKET_KICK.getBooleanValue()) {
                buf.skipBytes(buf.readableBytes());
                return;
            }
            throw e;
        }
    }
}
