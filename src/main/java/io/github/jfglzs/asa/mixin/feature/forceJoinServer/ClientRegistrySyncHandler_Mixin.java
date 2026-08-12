package io.github.jfglzs.asa.mixin.feature.forceJoinServer;

import io.github.jfglzs.asa.config.Configs;
import net.fabricmc.fabric.impl.client.registry.sync.ClientRegistrySyncHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientRegistrySyncHandler.class)
public class ClientRegistrySyncHandler_Mixin {
    @Inject(
            method = "apply",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void apply(CallbackInfo ci) {
        if (Configs.FORCE_JOIN_SERVER_IGNORE_UNKNOWN_PACKET.getBooleanValue()) {
            ci.cancel();
        }
    }
}
