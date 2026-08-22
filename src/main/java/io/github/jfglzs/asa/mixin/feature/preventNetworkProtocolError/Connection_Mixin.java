package io.github.jfglzs.asa.mixin.feature.preventNetworkProtocolError;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Connection.class)
public class Connection_Mixin {
    @WrapMethod(
            method = "genericsFtw"
    )
    private static void genericsFtw(Packet packet, PacketListener listener, Operation<Void> original) {
        try {
            original.call(packet, listener);
        }
        catch (Exception e) {
            if (!Configs.PREVENT_NET_PRO_ERR.getBooleanValue())
                throw e;
        }
    }
}
