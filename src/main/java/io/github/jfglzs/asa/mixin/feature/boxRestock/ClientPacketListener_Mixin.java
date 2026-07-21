package io.github.jfglzs.asa.mixin.feature.boxRestock;

import io.github.jfglzs.asa.feature.boxRestock.BoxRestockMannager;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListener_Mixin {
    @Inject(method = "handleContainerContent", at = @At("TAIL"))
    private void asa$restockBeforeRenderingShulkerScreen(
            ClientboundContainerSetContentPacket packet,
            CallbackInfo ci
    ) {
        if (BoxRestockMannager.isAutoRestockActive()) {
            System.out.println("[ASA BoxRestock] received container content packet");
            BoxRestockMannager.processOpenContainer();
        }
    }
}
