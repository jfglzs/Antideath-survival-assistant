package io.github.jfglzs.asa.mixin.event.screenEvents;

import io.github.jfglzs.asa.events.OpenScreenEvent;
import io.github.jfglzs.asa.utils.MCUtils;
import net.minecraft.client.gui.screens.Screen;
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
        Screen screen = MCUtils.getScreen();
        if (screen != null) {
            OpenScreenEvent.INSTANCE.update(screen);
        }
    }
}
