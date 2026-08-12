package io.github.jfglzs.asa.mixin.feature.disablePacketKick;

import io.github.jfglzs.asa.AsaMod;
import io.github.jfglzs.asa.config.Configs;
import io.github.jfglzs.asa.utils.MCUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.impl.networking.CommonRegisterPayload;
import net.fabricmc.fabric.impl.networking.RegistrationPayload;
import net.fabricmc.fabric.impl.registry.sync.packet.RegistrySyncPayload;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.common.CommonPacketTypes;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if < 1.21.8 {
//import net.minecraft.network.PacketSendListener;
//?}

@Mixin(Connection.class)
public class Connection_Mixin {
    @Shadow
    private Channel channel;

    @Inject(
            method = "sendPacket",
            at = @At("HEAD"),
            cancellable = true
    )
    //? if >= 1.21.8 {
    private void sendPacket(Packet<?> packet, ChannelFutureListener listener, boolean flush, CallbackInfo ci) {
    //?} else {
    //private void sendPacket(Packet<?> packet, PacketSendListener sendListener, boolean flush, CallbackInfo ci) {
    //?}
        if (Configs.DISABLE_PACKET_KICK_PREVENT_CUSTOM_PAYLOAD.getBooleanValue()
                && packet.type() == CommonPacketTypes.SERVERBOUND_CUSTOM_PAYLOAD
                && FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT
                && ! MCUtils.getMinecraft().isLocalServer()
        ) {
            ci.cancel();
        }
    }
}