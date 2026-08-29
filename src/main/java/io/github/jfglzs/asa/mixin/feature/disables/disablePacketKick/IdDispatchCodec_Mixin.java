package io.github.jfglzs.asa.mixin.feature.disables.disablePacketKick;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.feature.disablePacketKick.ASAFakePacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.IdDispatchCodec;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IdDispatchCodec.class)
public class IdDispatchCodec_Mixin<B extends ByteBuf, V> {
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
            if (Configs.Disables.DISABLE_PACKET_KICK.getBooleanValue()) {
                buf.skipBytes(buf.readableBytes());
                return (V) ASAFakePacket.INSTANCE;
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
            if (Configs.Disables.DISABLE_PACKET_KICK.getBooleanValue()) {
                buf.skipBytes(buf.readableBytes());
                return;
            }
            throw e;
        }
    }
}
