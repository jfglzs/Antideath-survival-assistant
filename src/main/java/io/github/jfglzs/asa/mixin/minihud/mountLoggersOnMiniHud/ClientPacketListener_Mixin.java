package io.github.jfglzs.asa.mixin.minihud.mountLoggersOnMiniHud;

import io.github.jfglzs.asa.accessor.IClientPacketListener;
import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(ClientPacketListener.class)
public class ClientPacketListener_Mixin implements IClientPacketListener {
    @Unique
    private List<String> asa$TabList;

    @Inject(
            method = "handleTabListCustomisation",
            at = @At("HEAD")
    )
    public void handleTabListCustomisation(ClientboundTabListPacket packet, CallbackInfo ci) {
        if (!Configs.MOUNT_LOGGERS_ON_MINIHUD.getBooleanValue()) return;
        this.asa$TabList = Arrays.asList(packet.footer().getString().split("\n"));
    }

    @Override
    public List<String> asa$TabList() {
        return asa$TabList;
    }
}
