package io.github.jfglzs.asa.mixin.feature.preventNetworkProtocolError;

import io.github.jfglzs.asa.config.Configs;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonPacketListenerImpl.class)
public class ClientCommonPacketListener_Mixin {
    @Inject(
            method = "onPacketError",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onPacketError(CallbackInfo ci) {
        if (Configs.PREVENT_NET_PRO_ERR.getBooleanValue()) {
            ci.cancel();
        }
    }
}
