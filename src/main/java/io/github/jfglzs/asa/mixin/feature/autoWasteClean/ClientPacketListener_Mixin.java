package io.github.jfglzs.asa.mixin.feature.autoWasteClean;

import io.github.jfglzs.asa.feature.autoWasteClean.AutoWasteCleanProcessor;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListener_Mixin {
    @Inject(
            method = "handleOpenScreen",
            at = @At("TAIL")
    )
    private void handleOpenScreen(ClientboundOpenScreenPacket packet, CallbackInfo ci) {
        if (MCUtils.getScreen() instanceof AbstractContainerScreen<?> screen) {
            AutoWasteCleanProcessor.process(screen);
        }
    }
}
