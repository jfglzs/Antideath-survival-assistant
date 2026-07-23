package io.github.jfglzs.asa.mixin.event.packetEvent;

import io.github.jfglzs.asa.feature.boxRestock.BoxRestockMannager;
import io.github.jfglzs.asa.feature.boxSplitter.BoxSplitter;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListener_Mixin {
    @Inject(
            method = "handleContainerContent",
            at = @At("TAIL")
    )
    private void handleContainerContent(ClientboundContainerSetContentPacket packet, CallbackInfo ci) {
        BoxRestockMannager.run();
        BoxSplitter.run();
    }
}
